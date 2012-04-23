#!/usr/bin/perl -w

@EXPORT = qw(findTypeKey findTypeDesc getType getDir);

my %message_type = (
    
    # message type          code  description       dir
    # initialization
    INIT_RQT            => [0x00, "initial message", 0],
    INIT_RSP            => [0x01, "initial response", 1],
    
    # resourceList
    LIST_RQT            => [0x10, "resourceList request", 0],
    LIST_RSP_NONVERB    => [0x11, "resourceList response, not verbose", 1],
    LIST_RSP_VERB       => [0x12, "resourceList response, verbose", 1],
    
    # resourceGet
    GET_RQT             => [0x20, "resourceGet request", 0],
    GET_RSP             => [0x21, "resourceGet response", 1],
    GET_ERR_UUID        => [0x2a, "resourceGet error: invalid UUID", 1],
    GET_ERR_ALLOC       => [0x2b, "resourceGet error: can't allocate resources", 1],
    
    # resourceRelease
    RELEASE_RQT         => [0x30, "resourceRelease request", 0],
    RELEASE_RSP         => [0x31, "resourceRelease response", 1],
    RELEASE_ERR_UUID    => [0x3a, "resourceRelease error: invalid UUID", 1],
    RELEASE_ERR_NOT_PROPERLY_RELEASED => [0x3b, "resourceRelease error: FPGA not responding", 1],
    
    # programResource
    PROGRAM_RQT         => [0x40, "programResource request", 0],
    PROGRAM_TRANS_RDY   => [0x41, "programResource transmission: ready for image", 1],
    PROGRAM_TRANS_CORR  => [0x42, "programResource transmission: image verified", 1],
    PROGRAM_ERR_TCP     => [0x4a, "programResource error: TCP socket failure", 1],
    PROGRAM_ERR_CRC     => [0x4b, "programResource error: image fails CRC check", 1],
    PROGRAM_ERR_CORRUPT => [0x4c, "programResource error: corrupt image", 1],
    PROGRAM_ERR_UUID    => [0x4d, "programResource error: invalid UUID", 1],
    PROGRAM_ERR_LENGTH  => [0x4e, "programResource error: file length mismatch", 1],
    
    # resourceStatus
    STATUS_RQT          => [0x50, "resourceStatus request", 0],
    STATUS_RSP          => [0x51, "resourceStatus response", 1],
    STATUS_ERR_UUID     => [0x52, "resourceStatus error: invalid UUID", 1],
    STATUS_ERR_INVALID  => [0x53, "resourceStatus error: incorrect response from fpga", 1],
    STATUS_ERR_TIMEOUT  => [0x54, "resourceStatus error: no response from fpga", 1],
    
    # set parameters
    SET_PARAMS_RQT	=> [0x60, "set parameters request", 0],
    SET_PARAMS_RSP	=> [0x61, "set parameters response", 1],
    SET_USER_REGS_RQT   => [0x62, "set the user accessible registers request", 0],
    SET_USER_REGS_RSP   => [0x63, "set the user accessible registers response", 1],
    GET_USER_REGS_RQT   => [0x64, "get the user accessible registers request", 0],
    GET_USER_REGS_RSP   => [0x65, "get the user accessible registers response", 1],
    ADD_VLAN_RQT        => [0x66, "add a vlan/IP to the requested resource", 0],
    ADD_VLAN_RSP        => [0x67, "added vlan and IP to resource", 1],
    REMOVE_VLAN_RQT     => [0x68, "remove entry for vlan specified", 0],
    REMOVE_VLAN_RSP     => [0x69, "vlan successfully removed", 1],
    VLAN_ERR_INVALID	=> [0x6a, "supplied vlanID invalid", 1],
    
    # resource register
    REGISTER_RQT        => [0xa0, "resource register request", 0],
    REGISTER_RSP        => [0xa1, "resource register response", 1],
    
    # termination
    TERM_RQT            => [0xe0, "termination request", 0],
    TERM_RSP            => [0xe1, "termination response", 1],
    TERM_SERVER_RQT     => [0xe2, "stop server request", 0],
    TERM_SERVER_RSP     => [0xe3, "stop server response", 1],
    TERM_ERR            => [0xea, "termination error: can't shut down", 1],
    
    # generic errors
    ERR_UNKNOWN         => [0xf0, "error: unknown error", 1],
    ERR_INVALID_TYPE    => [0xf1, "error: invalid type", 1],
    ERR_UNEXPECTED_TYPE => [0xf2, "error: unexpected type", 1],
    ERR_CRC             => [0xf3, "error: CRC mismatch", 1],
    ERR_LOCK            => [0xf4, "error: requested resource is locked", 1],
    ERR_NOT_INIT        => [0xf5, "error: server not initialized; send 0x00", 1],
    ERR_UNEXPECTED_CLT  => [0xf6, "error: unexpected client", 1],
    
);

# all the find* functions return undef if no type is found; the parameter is the
# type code

# returns the key
sub findTypeKey {
    
    my $key;
    
    foreach $key (sort keys %message_type) {
        return $key if ($message_type{$key}[0] == $_[0]);
    }
    
    return undef;
}

# returns the description
sub findTypeDesc {
    
    my $key;
    
    foreach $key (sort keys %message_type) {
        return $message_type{$key}[1] if ($message_type{$key}[0] == $_[0]);
    }
    
    return undef;
}

# returns the type code
sub getType {
    return $message_type {$_[0]}[0] if defined $message_type {$_[0]}[0];
    return undef;
}

# returns the direction
sub getDir {
    return $message_type {$_[0]}[2] if defined $message_type {$_[0]}[0];
    return undef;
}
