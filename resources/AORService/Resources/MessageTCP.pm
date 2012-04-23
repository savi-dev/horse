#!/usr/bin/perl -w

use String::CRC32;
@EXPORT = qw(receiveMessage sendMessage);

# receives a message from the supplied socket
# first parameter is the socket fd
sub receiveMessage {
    
    ($sock) = @_;
    
    # socket errors can happen wherever - check for its definedness every time
    # we use it
    return undef unless defined $sock -> connected;
    
    # 1 byte type code, 2 bytes payload size
    read $sock, $msg, 3;
    return undef unless defined $sock -> connected;
    ($type_id, $size) = unpack ("Cn", $msg);
    
    # payload, 4 bytes CRC
    read $sock, $msg, $size + 4;
    return undef unless defined $sock -> connected;
    
    # if no payload, just unpack the crc
    if ($size == 0) {
        ($crc) = unpack ("N", $msg);
        $payload = "";
        
    # else unpack the payload and the crc
    } else {
        ($payload, $crc) = unpack ("a${size}N", $msg);
    }
    
    # if crc mismatch, return an error
    return undef unless $crc == crc32 (pack ("Cna*", $type_id, $size, $payload));
    
    %message = (
        type => $type_id,
        payload => $payload,
    );
    
    # undefine all the things that could be reused
    $msg = $type_id = $size = $crc = $payload = undef;
    
    # return the message
    return %message;
    
}

# send a message from the supplied socket
sub sendMessage {
    
    ($sock, $type_id, $payload) = @_;
    return undef unless defined $sock -> connected;
    
    # if no payload is specified, make it blank
    $payload = "" unless defined $payload;
    
    # payload length
    $plen = do {use bytes; length $payload;};
    
    # 1 byte type id, 2 bytes payload length, payload, 4 bytes crc
    $msg = pack ("Cna*", $type_id, $plen, $payload);
    $crc = crc32 ($msg);
    $msg = pack ("a*N", $msg, $crc);
    
    # send the message through the socket
    print $sock $msg;
    
    # undefine all the things that could be reused
    $type_id = $plen = $payload = undef;
    
    return 1; 
}