#!/usr/bin/perl -w
use IO::Socket;
use IO::Select;
use String::CRC32;
use Data::UUID;
use MessageType;
use MessageTCP;
use Switch;
use Log::Log4perl qw(get_logger :levels);
use Sys::Hostname;
use POSIX ();

# set up logger
$logger = get_logger ();

# log everything including DEBUG level; change to $INFO or $WARNING as
# appropriate
$logger -> level ($DEBUG);

# log to screen; see log4perl documentation on how to log to a file instead, or
# just pipe it
$logappender = Log::Log4perl::Appender -> new (
    "Log::Dispatch::Screen"    
);

# set up log prefix: [date/time level] message newline
$loglayout = Log::Log4perl::Layout::PatternLayout -> new (
    "[%d %p] %m%n"
);
$logappender -> layout ($loglayout);
$logger -> add_appender ($logappender);

# set child signal handler, so we don't have to write any special code when
# spawning a thread to deal with the programResource request
$SIG{CHLD} = "IGNORE";

#ignore SIGHUP to prevent exiting an ssh session from terminating the program
local$SIG{HUP} = "IGNORE";

# global variables
my ($sock, $MAXLEN, $port, $msg, $tmpvar, $local_ip);

# maximum size of a packet
$MAXLEN = 65536;

# UUID wrapper object
$ug = new Data::UUID;

# iterate all the selectmap interfaces
# create hash of hash, keyed by the UUID
# this isn't strictly necessary since the selectmaps are fixed for a board
# anyway; also, interchips aren't exposed this way so you can't check it
################@maps = `cd /dev; ls selectmap*`;
################$i = 0;
################foreach $map (@maps) {
################    printf "device found: %s", $map;
################    
################    # generate random (v4) uuid
################    $u = $ug -> create_bin ();
################
################    $res[$i] = {
################        uuid => $u,
################        name => $map,
################    };
################    $i ++;
################}

#ignore SIGHUP
#
#my $sigset = POSIX::SigSet->new();
#my $action = POSIX::SigAction->new('sigHUP_ignore',
#                                    $sigset,
#                                    &POSIX::SA_NODEFER);
#POSIX::sigaction(&POSIX::SIGHUP, $action);



$logger -> info ("testing...");
# hardware nodes
for ($i = 0; $i < 4; $i ++) {
    
    $fc [$i] = {
        
        # uuid of the hardware node, created on the spot based on the namespace
        # and name combination - is still random since it uses the current
        # unix epoch time; uuids are ALWAYS generated in binary format because
        # it's easier to transmit
        uuid => $ug -> create_from_name_bin ("AOR-namespace", "selectmap" . ($i + 1)),
        
        # the unix filesystem entrypoint
        resource => "selectmap" . ($i + 1),
        
        # the name exposed to the web service
        name => "fpga" . ($i + 1),
        
        # initial ip
        ip => [0, 0, 0, 0],
        
        # default image name
        #default_image => "defimg" . ($i + 1),
	default_image => "default.bit"
    }
    
    #open(PROCHANDLE, ">/proc/fpga/selectmap$i");
    #open(BITHANDLE, "<default.bit");
    #open(DEVHANDLE, ">/dev/selectmap$i");
    #print PROCHANDLE "00000000";
    #print PROCHANDLE "04000000";
    #while (<BITHANDLE>) {
   # 	print DEVHANDLE $_;
   # }
   # print PROCHANDLE "0F000000";
   # close(PROCHANDLE);
   # close(BITHANDLE);
   # close(DEVHANDLE);
    
}

my $output = qx/\/sbin\/ifconfig eth0/;

my @lines = split(/\n/, $output);
foreach my $line (@lines) {
	if($line =~ m/inet addr:\s*(\S+)\s+.*$/) {
		$local_ip = $1;
	}
}

foreach $node (@fc) {
    system("echo 00000000 > /proc/fpga/$node->{resource}");
    system("echo 04000000 > /proc/fpga/$node->{resource}");
    system("cat default.bit > /dev/$node->{resource}");
    system("echo 0F000000 > /proc/fpga/$node->{resource}");
}
# interchip nodes
for ($i = 0; $i < 4; $i ++) {
    
    $ic [$i] = {
        
        # uuid of the interchip node, see explanation for hardware node above
        uuid => $ug -> create_from_name_bin ("AOR-namespace", "interchip" . ($i + 1)),
        
        # the name exposed to the web service
        name => "interchip" . ($i + 1),
        
        # the uuids of hardware nodes that the interchip links, [left, right]
        link => [$fc [$i % 4] -> {uuid}, $fc [($i + 1) % 4] -> {uuid}],
    }
}

#foreach $r (@ic) {
#    $logger->info("Created name: $r->{name}\, uuid: $r->{uuid}.\n");
#}

$useTCP = 1;

if (defined $useTCP) {

# declare the port
$server_port = (defined $ARGV [0] ? $ARGV [0] : 51001);

# name
# $server_name = (defined $ARGV [1] ? $ARGV [1] : "bee2_new");
if($local_ip =~ m/^192.168.1.61$/) {
	$server_name = "bee2-1";
} elsif($local_ip =~ m/^192.168.1.62$/) {
	$server_name = "bee2-2";
} elsif($local_ip =~ m/^192.168.1.63$/) {
	$server_name = "bee2-3";
} elsif($local_ip =~ m/^192.168.1.64$/) {
	$server_name = "bee2-4";
} elsif($local_ip =~ m/^192.168.1.65$/) {
	$server_name = "bee2-5";
} elsif($local_ip =~ m/^192.168.1.66$/) {
	$server_name = "bee2-6";
} elsif($local_ip =~ m/^192.168.1.67$/) {
	$server_name = "bee2-7";
} elsif($local_ip =~ m/^192.168.1.68$/) {
	$server_name = "bee2-8";
} elsif($local_ip =~ m/^192.168.1.69$/) {
	$server_name = "bee2-9";
} elsif($local_ip =~ m/^192.168.1.70$/) {
	$server_name = "bee2-10";
}


$broadcast_sock = IO::Socket::INET -> new (
                                 PeerPort => 41001,
                                 PeerAddr => inet_ntoa (INADDR_BROADCAST),
				 #PeerAddr => '127.0.0.1',
                                 Proto => 'udp',
                                 Broadcast => 1)
    or die "broadcast socket: $@";

# open the server socket
$server_sock = IO::Socket::INET -> new (LocalPort => $server_port,
                                 Type => SOCK_STREAM,
                                 Listen => 1,
                                 Proto => 'tcp')
    or die "socket: $@";
    
# broadcast the packet
$server_name_length = do {use bytes; length $server_name};
$broadcast_packet = pack ("Ca*nN", $server_name_length, $server_name, $server_port);

my($read_set) = new IO::Select();
$read_set->add($server_sock);
my($need_continue) = 1;

while($need_continue) {
	print "Sending broadcast packet\n";
	$broadcast_sock -> send ($broadcast_packet);
	
	#wait for 10 seconds
	my($rh_set) = IO::Select->select($read_set, undef, undef, 3);

	foreach $rh (@$rh_set) {
		if($rh == $server_sock) {
			$need_continue = 0;
			last;
		}
	}

}
close ($broadcast_sock);

    
$logger -> info ("Socket established on port ${server_port}.");

# main message processing loop
$continue = 1;
while ($continue) {
    
    $logger -> debug ("Waiting for new connection...");

    # receive connection from the web service    
    $sock = $server_sock -> accept ();
    binmode $sock;
    $sock -> autoflush (1);

    $logger->debug("Received connection on ${server_port}.");
    
    # obtain the port and address of the web service
    ($sock_port, $sock_addr) = sockaddr_in ($sock -> peername);
    $sock_ip = inet_ntoa ($sock_addr);
    
    # wait for an INIT_RQT message; if undef is returned, it means that the
    # message received is corrupt (CRC error)
    unless (%m = receiveMessage ($sock)) {
        
        # reply that crc was in error
        sendMessage ($sock, getType ("ERR_CRC"), undef);
        
        # this may be kinda severe... but it'll never happen anyway
        close ($sock);
        next;
    }
    
    # first message after connection MUST be of type INIT_RQT, otherwise the
    # connection is invalid
    unless (findTypeKey ($m {type}) eq "INIT_RQT") {
        
        # reply that the message type was not expected
        sendMessage ($sock, getType ("ERR_NOT_INIT"), undef);
        
        # close the sock since it's invalid
        close ($sock);
        next;
    }
    $logger -> debug ("Initiating connection...\n");
    
    # send the response for INIT_RQT
    sendMessage ($sock, getType ("INIT_RSP"), undef);
    
    $logger -> info ("Connection to ${sock_ip}:${sock_port} established.");
    
    # now start processing real messages
    while (1) {
        
        # socket fault? terminate
        unless (defined $sock -> connected) {
            close ($sock);
            $logger -> error ("Connection to ${sock_ip}:${sock_port} terminated due to client error.");
            last;
        }
        
        # wait for a message
        unless (%m = receiveMessage ($sock)) {
            
            # socket errors can occur at any time, so need to check it every
            # time we do something with it
            unless (defined $sock -> connected) {
                close ($sock);
                $logger -> error ("Connection to ${sock_ip}:${sock_port} terminated due to client error.");
                last;
            }
            $logger -> error ("Received packet has wrong CRC, discarded.");
            sendMessage ($sock, getType ("ERR_CRC"), undef);
            next;
        }

        # parse message type
        # findTypeKey returns undef if the type is not found; otherwise, if the
        # type has the wrong direction (i.e. the message should have been sent
        # from the subagent instead), that's also not right
        
        # the packet is recognised and the appropriate function is called to
        # manage it
        
        # all functions take zero parameters
        if (defined ($type = findTypeKey ($m {type})) && getDir ($type) == 0) {
            $cmdstr = 'proc' . $type;
            $logger -> info ("Received packet with type $type.");
            
        # the direction is wrong
        } elsif (getDir ($type) == 1) {
            $cmdstr = 'procUnexpectedMsg';
            $logger -> info ("Received packet with type $type (unexpected).");
            
        # we don't recognise the packet type
        } else {
            $cmdstr = 'procUnknownMsg';
            $logger -> info ("Received packet with unknown type.");
        }
        
        # call the function; if the function returns false, then the processing
        # failed
        if (!eval ($cmdstr)) {
          
            # kill the connection if the message processing fails, most likely
            # due to a broken socket
            $logger -> info ("Connection to ${sock_ip}:${sock_port} terminated.");
            last;
        };
        
        
    }
  }
 } #end if (defined $useTCP)
 else {
	while (1) {
		print "Enter Message Type: ";
		chomp($m {type} = <>);
		print "Enter Device you would like to access: ";
		chomp($deviceID = <>);

		$m{payload} = $fc[$deviceID]->{uuid};

		$logger->info("sending message of type: $m{type}.\n");
		
        	# parse message type
        	# findTypeKey returns undef if the type is not found; otherwise, if the
        	# type has the wrong direction (i.e. the message should have been sent
        	# from the subagent instead), that's also not right
        
       		# the packet is recognised and the appropriate function is called to
        	# manage it
        
        	# all functions take zero parameters
        	if (defined ($type = findTypeKey ($m {type})) && getDir ($type) == 0) {
       		     $cmdstr = 'proc' . $type;
       		     $logger -> info ("Received packet with type $type.");
            
       		 # the direction is wrong
       		 } elsif (getDir ($type) == 1) {
       		     $cmdstr = 'procUnexpectedMsg';
       		     $logger -> info ("Received packet with type $type (unexpected).");
       		     
       		 # we don't recognise the packet type
       		 } else {
       		     $cmdstr = 'procUnknownMsg';
       		     $logger -> info ("Received packet with unknown type.");
       		 }
        
       		 # call the function; if the function returns false, then the processing
       		 # failed
       		 if (!eval ($cmdstr)) {
       	   
       		     # kill the connection if the message processing fails, most likely
       		     # due to a broken socket
       		     $logger -> info ("Error, terminating program");
       		     last;
       		 };
	}#end while (1);	

 }



sub procADD_VLAN_RQT {
        return sendMessage ($sock, getType ("ADD_VLAN_RSP"), undef);
}

sub procREMOVE_VLAN_RQT {
        return sendMessage ($sock, getType ("REMOVE_VLAN_RSP"), undef);
}


# processes program request

# This function opens a child process to deal with the transmission of the
# bitstream. The child process passes information back to the parent process
# which processes it as appropriate.

# The messages passed are:

# tcpfail - the child socket failed
# tcpready - the child socket has been opened and is ready for transmission
# filefail - the filehandle to write the bitstream to could not be opened
# complete - the bitstream has been entirely transferred. Two message follow:
#   1. the length of the bitstream that has been received.
#   2. the CRC of the bitstream.
# These two messages are verified by the parent process to make sure the image
# was not corrupt.
# after the transmission of the complete and the two subsequent messages, the
# child process finishes. The parent process write the bitstream to the fpga and
# verifies that it succeeded.

sub procPROGRAM_RQT {

    @ip = ();
    
    # split up the payload
    ($uuid, $img_size, $img_crc, $ip[0], $ip[1], $ip[2], $ip[3]) = unpack ("a16N2C4", $m{payload});
    
    # check the uuid
    
    # rr is the resource endpoint in the filesystem
    $rr = undef;
    
    # rn is the resource name
    $rn = undef;
    
    foreach $r (@fc) {
        
        # uuid matches?
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            
            $rr = $r -> {resource};
            $rn = $r -> {name};
            @{$r -> {ip}} = @ip;
            last;
        }
    }
    
    # can't match a uuid
    if (!$rr) {
        $logger -> error ("PROGRAM: UUID ${uuid} not found.");
        return sendMessage ($sock, getType ("PROGRAM_ERR_UUID"), undef);
    }
    
    $logger -> info ("PROGRAM: UUID verified: ${rn}, initiating download.");
    
    # spawn the child process to handle TCP
    $tcpport = $server_port + 1;
        
    # parent process; wait until TCP is complete
    # the open function with the "-|" parameter makes the child thread spawn the
    # exact identical code as the parent process, including the program counter
    # so it'll spawn at this point. For the parent process the open () returns
    # the process id of the child, while for the child process it returns 0.
    # The child process' STDIN/STDOUT are directed to the CHILD filehandle of
    # the parent, so parent writing to the CHILD arrives at STDIN of the child,
    # while child writing to STDOUT arrives at parent's CHILD process.
    if (open (CHILD, "-|")) {

        # select returns a list of filehandles that are ready for read/write, or
        # have errors; select (readset, writeset, errorset, timeout)
        # set up select call
        $rs = new IO::Select ();
        $rs -> add ($sock);
        $rs -> add (\*CHILD);

        $complete = 0;
        until ($complete) {
            
            # timeout for 0.5 seconds... probably not necessary, we can do this
            # indefinitely until SOME filehandle becomes available for write
            @rhs = IO::Select -> select ($rs, undef, undef, 0.5);
            
            foreach $rh (@rhs) {
                
                # we have a packet over the original connection
                if ($rh == $sock) {
                    
                    %m = readMessage ($sock);
                    $logger -> info ("PROGRAM: Notifying client that system is locked.");
                    sendMessage ($sock, getType ("ERR_LOCK"), undef);
                    
                # we have data over the CHILD filehandle
                } else {
                    
                    $lin = <CHILD>;
                    
                    # the stdin pipe is broken or closed
                    last unless (defined $lin);
                    
                    # Right now this line catches both tcpfail and filefail
                    # The child quits immediately after transferring tcpfail
                    # or filefail
                    if ($lin =~ /fail/) {
                        
                        $logger -> error ("PROGRAM: Program socket failure on port ${tcpport}.");
                        return sendMessage ($sock, getType ("PROGRAM_ERR_TCP"), undef);
                    
                    # the child process indicates that the program socket is
                    # ready for transmission. The parent sends the web service
                    # the TRANS_RDY message with the port number. The web
                    # service then connects to the port and starts streaming the
                    # file.
                    } elsif ($lin =~ /tcpready/) {
                        
                        $logger -> info ("PROGRAM: Program socket listening on port ${tcpport}.");
                        sendMessage ($sock, getType ("PROGRAM_TRANS_RDY"), pack ("n", $tcpport));
                        
                    # the child process indicates that the program socket has
                    # finished transferring the file. The child process then
                    # transfers the file length and the file CRC.
                    } elsif ($lin =~ /complete/) {
                    
                        $logger -> info ("PROGRAM: file transfer complete.");
                        
                        # first line contains file length
                        $flen = <CHILD>;
                        
                        # mismatch in file length
                        if ($flen != $img_size) {
                            
                            # delete the file
                            `rm -rf $rn.bit`;
                            $logger -> error ("PROGRAM: transferred file has length mismatch (expecting ${img_size}, got ${flen}). Transfer aborted.");
                            return sendMessage ($sock, getType ("PROGRAM_ERR_LENGTH"), undef);
                        }
                        
                        # second line contains file CRC
                        $fcrc = <CHILD>;
                        
                        # mismatch in CRC
                        if ($fcrc != $img_crc) {
                            
                            # delete the file
                            `rm -rf $rn.bit`;
                            $logger -> error ("PROGRAM: transferred file has CRC mismatch (expecting ${img_crc}, got ${fcrc}). Transfer aborted.");
                            return sendMessage ($sock, getType ("PROGRAM_ERR_CRC"), undef);
                        }
                        
                        # quit the loop
                        $complete = 1;
                        last;
                    } else {
                        
                        # unexpected message from the child process
                        $logger -> info ("PROGRAM<CHILD>: ${lin}");
                    
                    } # end of <CHILD> type check
                } # end of FILEHANDLE check
            } # end of FILEHANDLE loop
        } # end of blocked loop
        
        close (CHILD);


	#clear lingering selectmap values
    	$numWaiting = getReadCount(${rr}); 
    	open(deviceHandle, "</dev/${rr}");
   	while($numWaiting) {
		read(deviceHandle, $buf, 1);
		$output = "Read Value: " . unpack("C", $buf) . "\n";
		$logger->debug("$output");
		$numWaiting = getReadCount(${rr});
	}
	close(deviceHandle);
	
        
        # write the file
        `echo 00000000 > /proc/fpga/$rr`; # reset the fpga
        `echo 04000000 > /proc/fpga/$rr`; # set the fpga on programming mode
        `cat  $rn.bit  > /dev/$rr`      ; # send the bitstream to the fpga
        `echo 0f000000 > /proc/fpga/$rr`; # only necessary for FIFO
        
        $logger -> info ("PROGRAM: file saved as ${rn}.bit.");
        
        # check fpga status - not currently implemented
        # if not good:
        #$logger -> error ("PROGRAM: image file is corrupt.");
        #$return sendMessage ($sock, getType ("PROGRAM_TRANS_CORRUPT"), undef);
        
        # if good:
        
        $logger -> info ("PROGRAM: file verified and transferred to ${rn}.");
        return sendMessage ($sock, getType ("PROGRAM_TRANS_CORR"), undef);
        
    # child process: spawn TCP
    } else {
        local$SIG{HUP} = \%sigHUP;
        
        # open a TCP port
        unless ($tcpsock = IO::Socket::INET -> new (Listen => 1,
                                                    Type => SOCK_STREAM,
                                                    LocalPort => $tcpport,
                                                    Proto => "tcp")) {
            print "tcpfail\n";
            exit;
        }
        
        # TCP port is now open; tell parent thread that we're ready to accept 
        # connections
        print "tcpready\n";
        
        # i'm only gonna accept one client anyway, no need for a while loop
        $client = $tcpsock -> accept ();
        binmode $client;
        
        # set up output file
        unless (open FOUT, ">$rn.bit") {
            print "filefail\n";
            exit;
        }
        
        # bytesread carries the number of bytes read from the socket on each
        # invocation of read
        # bytecount is the total number of bytes
        $bytecount = 0;
        
        # read from the socket, maximum 1024 bytes
        while (($bytesread = read $client, $line, 1024) != 0) {
            
            # increment the count
            $bytecount += $bytesread;
            
            # write the contents to the file
            print FOUT $line;
        } # eof
        
        # close and tear-down the TCP connection
        close ($client);
        close ($tcpsock);
        close (FOUT);
        
        # tell the parent process that it's complete
        print "complete\n";
        
        # the file length
        printf "%d\n", $bytecount;
        
        open (TEMPFILE, "$rn.bit");
        # the file CRC (crc32 invoked on a filehandle calculates the crc for the
        # file)
	# note: need to write the output as UNSIGNED
	printf "%u\n", crc32 (*TEMPFILE);
        close (TEMPFILE);
        
        exit;

    }
         
}

#processes resource release request
sub procRELEASE_RQT {
    
    $uuid = $m{payload};
    $rr = undef;
    $rdi = "default.bit";
    
    # find the uuid corresponding to the payload
    foreach $r (@fc) {
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            $rr = $r -> {resource};
            
            # rdi is the default image file (must be stored on the system
            # already)
            $rdi = $r -> {default_image};
            last;
        }
    }
    
    # if uuid isn't found, return the error
    unless (defined $rr) {
        $logger -> error ("RELEASE: UUID {uuid} not found.");
        return sendMessage ($sock, getType ("RELEASE_ERR_UUID"), $uuid);
    }
    
    
    # found uuid, release resource
    # default image
    $logger->debug("echo 00000000 > /proc/fpga/$rr");
    $logger->debug("echo 04000000 > /proc/fpga/$rr");
    $logger->debug("cat $rdi  > /dev/$rr");
    $logger->debug("echo 0f000000 > /proc/fpga/$rr");

    system("echo 00000000 > /proc/fpga/$rr");
    system("echo 04000000 > /proc/fpga/$rr");
    system("cat $rdi > /dev/$rr");
    system("echo 0f000000 > /proc/fpga/$rr");
    $numWaiting = getReadCount(${rr}); 
    if($numWaiting < 0) {
    	return sendMessage ($sock, getType ("RELEASE_ERR_NOT_PROPERLY_RELEASED"), undef);
    } elsif ($numWaiting > 129) {
    	return sendMessage ($sock, getType ("RELEASE_ERR_NOT_PROPERLY_RELEASED"), undef);
    } else {
    	return sendMessage ($sock, getType ("RELEASE_RSP"), undef);
    }
    
    
}

sub getReadCount {
	$rr = $_[0];
	open(FILEHANDLE, "/proc/fpga/$rr");
	$logger->error("Unable to open /proc/fpga/$rr\n") unless (defined FILEHANDLE);
	@DAT = <FILEHANDLE>;
	foreach $line (@DAT) {
		if($line =~ m/Mode: (.*)\n/i) {
			if($1 ne "FIFO") {
				return -1;
			}
		}
		if($line =~ m/Read count: (\d*).*/) {
			$numWaiting = $1;
			last;
		}
	}
	close(FILEHANDLE);

	return $numWaiting;
}

# processes resource status request
sub procSTATUS_RQT {
    
    $uuid = $m{payload};
    $rr = undef;
    
    # find the uuid corresponding to the payload
    foreach $r (@fc) {
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            $rr = $r -> {resource};
            last;
        }
    }
    
    # if uuid isn't found, return the error
    unless (defined $rr) {
        $logger -> error ("STATUS: UUID ${uuid} not found.");
        return sendMessage ($sock, getType ("STATUS_ERR_UUID"), $uuid);
    }
    
    # query the resource $rr here - not implemented yet
    $res_status = 0;
    
    $numWaiting = getReadCount(${rr}); 
    if($numWaiting < 0) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    } elsif ($numWaiting > 129) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    }
    open(deviceHandle, "</dev/${rr}");
    while($numWaiting) {
	read(deviceHandle, $buf, 1);
	$output = "Read Value: " . unpack("C", $buf) . "\n";
	$logger->debug("$output");
	$numWaiting = getReadCount(${rr});
    }
    close(deviceHandle);

    $logger->info("Drained lingering values from selectmap\n");

    #Send request for data
    open(deviceHandle, ">/dev/${rr}");
    print deviceHandle pack("C", 49);
    $logger->debug(sprintf("Sent 49 to the selectmap\n"));
    close(deviceHandle);

    $i = 0;
    while ($i < 100) {
    	#pause for 0.1 seconds
    	select(undef, undef, undef, 0.1);
	
	if($numWaiting = getReadCount(${rr})) {
	    open(deviceHandle, "</dev/${rr}");
	    read(deviceHandle, $buf, 1);
	    if (unpack("C", $buf) == 45) {
	        $logger->info("Received response from fpga\n");
		$res_status = 0;
	    } else {	    
		$logger->error("Received incorrect response from fpga\n");
		$res_status = 1;
	    }
	}
	$i = $i + 1;
	last if $numWaiting;
    }
    if(!$numWaiting) {	
	$logger->error("Did not receive a response from the fpga\n");
	$res_status = 2;
    }
    close(deviceHandle);
	    
    $logger -> info ("STATUS: pinging ${rr} returned status ${res_status}.");

    if($res_status == 0) {
	sendMessage ($sock, getType ("STATUS_RSP"), pack ("n", $res_status));
    } elsif ($res_status == 1) {
	sendMessage ($sock, getType ("STATUS_ERR_INVALID"), pack ("n", $res_status));
    } elsif ($res_status == 2) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
    }
}

# processes resource register request
sub procREGISTER_RQT {
    
    # number of hardware nodes and the number of interchip nodes
    $pl = pack ("CC", scalar @fc, scalar @ic);
    
    # for each hardware node, return the uuid and the name
    foreach $r (@fc) {
        
        # get the number of bytes in the name
        $nl = do {use bytes; length $r -> {name}};
        
        # first uuid, then the length of the name, then the name itself
        $pl = pack ("a*a*Ca*", $pl, $r -> {uuid}, $nl, $r -> {name});
    }
    
    # for each interchip node, return the uuid, the name, and the linked fpgas
    foreach $r (@ic) {
        
        # get the number of bytes in the name
        $nl = do {use bytes; length $r -> {name}};
        
        # first uuid, then the length of the name, then the name itself, then
        # the two links' uuids
        $pl = pack ("a*a*Ca*a*a*", $pl, $r -> {uuid}, $nl, $r -> {name}, $r -> {link}[0], $r -> {link}[1]);
    }
    
    # send back
    $logger -> info ("REGISTER: resource registration completed.");
    return sendMessage ($sock, getType ("REGISTER_RSP"), $pl);
}

# processes resource set parameters request
sub procSET_PARAMS_RQT {

    @ip = ();
    @vlan = ();
    
    # split up the payload
    ($uuid, $ip[0], $ip[1], $ip[2], $ip[3], $vlanID) = unpack ("a16C4N1", $m{payload});
    
    if($vlanID < 0 || $vlanID > 65536) {
    	$logger -> error ("SET_PARAMS: vlanID invalid.");
        return sendMessage ($sock, getType ("VLAN_ERR_INVALID"), $uuid);
    }
    ($vlan[0], $vlan[1], $vlan[2], $vlan[3]) = unpack("C4", pack("N", $vlanID));

    $logger -> debug ("vlanID = $vlanID, vlan[0] = $vlan[0], vlan[1] = $vlan[1], vlan[2] = $vlan[2], vlan[3] = $vlan[3]");
    
    $uuid = $m{payload};
    $rr = undef;
    
    # find the uuid corresponding to the payload
    foreach $r (@fc) {
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            $rr = $r -> {resource};
            last;
        }
    }
    
    # if uuid isn't found, return the error
    unless (defined $rr) {
        $logger -> error ("SET_PARAMS: UUID ${uuid} not found.");
        return sendMessage ($sock, getType ("STATUS_ERR_UUID"), $uuid);
    }
    
    # query the resource $rr here - not implemented yet
    $res_status = 0;
    
    $numWaiting = getReadCount(${rr}); 
    if($numWaiting < 0) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    } elsif ($numWaiting > 129) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    }
    open(deviceHandle, "</dev/${rr}");
    while($numWaiting) {
	read(deviceHandle, $buf, 1);
	$output = "Read Value: " . unpack("C", $buf) . "\n";
	$logger->debug("$output");
	$numWaiting = getReadCount(${rr});
    }
    close(deviceHandle);

    $logger->info("Drained lingering values from selectmap\n");

    #Send request for data
    open(deviceHandle, ">/dev/${rr}");
    print deviceHandle pack("C", 0x60);
    print deviceHandle pack("C", $vlan[2]);
    print deviceHandle pack("C", $vlan[3]);
    $logger->debug(sprintf("Sent 0x60 to the selectmap\n"));
    close(deviceHandle);

    $i = 0;
    while ($i < 100) {
    	#pause for 0.1 seconds
    	select(undef, undef, undef, 0.1);
	
	if($numWaiting = getReadCount(${rr})) {
	    open(deviceHandle, "</dev/${rr}");
	    read(deviceHandle, $buf, 1);
	    $c = unpack("C", $buf);
	    if (unpack("C", $buf) == 0x61) {
	        $logger->info("Received response from fpga\n");
		$res_status = 0;
	    } else {	    
		$logger->error("Received incorrect response from fpga: $c\n");
		$res_status = 1;
	    }
	}
	$i = $i + 1;
	last if $numWaiting;
    }
    if(!$numWaiting) {	
	$logger->error("Did not receive a response from the fpga\n");
	$res_status = 2;
    }
    close(deviceHandle);
	    
    $logger -> info ("SET_PARAMS: setting ${rr} returned status ${res_status}.");

    if($res_status == 0) {
	sendMessage ($sock, getType ("SET_PARAMS_RSP"), pack ("n", $res_status));
    } elsif ($res_status == 1) {
	sendMessage ($sock, getType ("STATUS_ERR_INVALID"), pack ("n", $res_status));
    } elsif ($res_status == 2) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
    }
}

# processes resource set user registers request
sub procSET_USER_REGS_RQT {

    @regVals = ();

    $logger->debug("Entering SET_USER_REGISTERS_RQT function\n");
    
    # split up the payload
    ($uuid, $regVal) = unpack ("a16a4", $m{payload});

    $rr = undef;
    
    # find the uuid corresponding to the payload
    foreach $r (@fc) {
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            $rr = $r -> {resource};
            last;
        }
    }
    
    # if uuid isn't found, return the error
    unless (defined $rr) {
        $logger -> error ("SET_PARAMS: UUID ${uuid} not found.");
        return sendMessage ($sock, getType ("STATUS_ERR_UUID"), $uuid);
    }
    
    # query the resource $rr here - not implemented yet
    $res_status = 0;
    

    $logger->debug("Starting drain of extra values\n");
    $numWaiting = getReadCount(${rr}); 
    if($numWaiting < 0) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    } elsif ($numWaiting > 129) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    }
    open(deviceHandle, "</dev/${rr}");
    while($numWaiting) {
	read(deviceHandle, $buf, 1);
	$output = "Read Value: " . unpack("C", $buf) . "\n";
	$logger->debug("$output");
	$numWaiting = getReadCount(${rr});
    }
    close(deviceHandle);

    $logger->info("Drained lingering values from selectmap\n");

    #Send request for data
    open(deviceHandle, ">/dev/${rr}");
    print deviceHandle pack("C", 0x62);
    print deviceHandle pack("a4", $regVal);
    $logger->debug(sprintf("Sent 0x62 to the selectmap\n"));
    close(deviceHandle);

    $i = 0;
    while ($i < 100) {
    	#pause for 0.1 seconds
    	select(undef, undef, undef, 0.1);
	
	if($numWaiting = getReadCount(${rr})) {
	    open(deviceHandle, "</dev/${rr}");
	    read(deviceHandle, $buf, 1);
	    $c = unpack("C", $buf);
	    if (unpack("C", $buf) == 0x63) {
	        $logger->info("Received response from fpga\n");
		$res_status = 0;
	    } else {	    
		$logger->error("Received incorrect response from fpga: $c\n");
		$res_status = 1;
	    }
	}
	$i = $i + 1;
	last if $numWaiting;
    }
    if(!$numWaiting) {	
	$logger->error("Did not receive a response from the fpga\n");
	$res_status = 2;
    }

    close(deviceHandle);
    $logger -> info ("SET_USER_REGS: setting ${rr} returned status ${res_status}.");

    if($res_status == 0) {
	sendMessage ($sock, getType ("SET_USER_REGS_RSP"), pack ("n", $res_status));
    } elsif ($res_status == 1) {
	sendMessage ($sock, getType ("STATUS_ERR_INVALID"), pack ("n", $res_status));
    } elsif ($res_status == 2) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
    }
}

# processes get user registers request
sub procGET_USER_REGS_RQT {

    @regVals = ();
    
    # split up the payload
    ($uuid) = unpack ("a16", $m{payload});

    
    $uuid = $m{payload};
    $rr = undef;
    
    # find the uuid corresponding to the payload
    foreach $r (@fc) {
        if ($ug -> compare ($uuid, $r -> {uuid}) == 0) {
            $rr = $r -> {resource};
            last;
        }
    }
    
    # if uuid isn't found, return the error
    unless (defined $rr) {
        $logger -> error ("SET_PARAMS: UUID ${uuid} not found.");
        return sendMessage ($sock, getType ("STATUS_ERR_UUID"), $uuid);
    }
    
    # query the resource $rr here - not implemented yet
    $res_status = 0;
    
    $numWaiting = getReadCount(${rr}); 
    if($numWaiting < 0) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    } elsif ($numWaiting > 129) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
	return 1;
    }
    open(deviceHandle, "</dev/${rr}");
    while($numWaiting) {
	read(deviceHandle, $buf, 1);
	$output = "Read Value: " . unpack("C", $buf) . "\n";
	$logger->debug("$output");
	$numWaiting = getReadCount(${rr});
    }
    close(deviceHandle);

    $logger->info("Drained lingering values from selectmap\n");

    #Send request for data
    open(deviceHandle, ">/dev/${rr}");
    print deviceHandle pack("C", 0x64);
    #print deviceHandle pack("C", $regVal[0]);
    #print deviceHandle pack("C", $regVal[1]);
    #print deviceHandle pack("C", $regVal[2]);
    #print deviceHandle pack("C", $regVal[3]);
    $logger->debug(sprintf("Sent 0x64 to the selectmap\n"));
    close(deviceHandle);

    $i = 0;
    while ($i < 100) {
    	#pause for 0.1 seconds
    	select(undef, undef, undef, 0.1);
	
	if($numWaiting = getReadCount(${rr})) {
	    open(deviceHandle, "</dev/${rr}");
	    read(deviceHandle, $buf, 5);
	    ($c, $regVal[0], $regVal[1], $regVal[2], $regVal[3]) = unpack("C5", $buf);
	    $logger->debug("Got regvals = 0: $regVal[0] , 1: $regVal[1] , 2: $regVal[2] , 3: $regVal[3]\n");
	    if ($c == 0x65) {
	        $logger->info("Received response from fpga\n");
		$res_status = 0;
	    } else {	    
		$logger->error("Received incorrect response from fpga: $c\n");
		$res_status = 1;
	    }
	}
	$i = $i + 1;
	last if $numWaiting;
    }
    if(!$numWaiting) {	
	$logger->error("Did not receive a response from the fpga\n");
	$res_status = 2;
    }

    close(deviceHandle);
	    
    $logger -> info ("SET_USER_REGS: setting ${rr} returned status ${res_status}.");

    if($res_status == 0) {
	sendMessage ($sock, getType ("GET_USER_REGS_RSP"), pack ("C4", $regVal[0], $regVal[1], $regVal[2], $regVal[3]));
    } elsif ($res_status == 1) {
	sendMessage ($sock, getType ("STATUS_ERR_INVALID"), pack ("n", $res_status));
    } elsif ($res_status == 2) {
	sendMessage ($sock, getType ("STATUS_ERR_TIMEOUT"), pack ("n", 1));
    }
}

# processes termination request
sub procTERM_RQT {
    
    $logger -> info ("TERMINATE: Shutting down connection...");
    sendMessage ($sock, getType ("TERM_RSP"), undef);
    close ($sock);
    return undef;
}

# processes server stop request
sub procTERM_SERVER_RQT {
    
    $logger -> info ("TERMINATE: Shutting down server...");
    sendMessage ($sock, getType ("TERM_SERVER_RSP"), undef);
    close ($sock);
    close ($server_sock);
    return ($continue = undef);
    
}

# processes unexpected messages
sub procUnexpectedMsg {
    
    sendMessage ($sock, getType ("ERR_UNEXPECTED_TYPE"), undef);
    
}

# processes unknown messages
sub procUnknownMsg {
    
    sendMessage ($sock, getType ("ERR_INVALID_TYPE"), undef);
    
}

#ignore handler
sub sigHUP {
    $logger-> debug("Got ignore within child");
    exit;
        
}

