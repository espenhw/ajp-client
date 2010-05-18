package org.grumblesmurf.ajp;

enum AjpMethod 
{
    OPTIONS(1),
    GET(2),
    HEAD(3),
    POST(4),
    PUT(5),
    DELETE(6),
    TRACE(7),
    PROPFIND(8),
    PROPPATCH(9),
    MKCOL(10),
    COPY(11),
    MOVE(12),
    LOCK(13),
    UNLOCK(14),
    ACL(15),
    REPORT(16),
    VERSION_CONTROL(17),
    CHECKIN(18),
    CHECKOUT(19),
    UNCHECKOUT(20),
    SEARCH(21),
    MKWORKSPACE(22),
    UPDATE(23),
    LABEL(24),
    MERGE(25),
    BASELINE_CONTROL(26),
    MKACTIVITY(27);

    final int code;
    
    private AjpMethod(int code) {
        this.code = code;
    }
}
