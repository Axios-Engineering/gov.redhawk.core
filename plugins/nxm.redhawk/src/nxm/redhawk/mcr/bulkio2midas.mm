STARTMACRO t:corbaargs t:fftargs t:thinargs s:output
    PIPE INIT
        corbareceiver/MSGID=MAIN/TLL=200/PS=^corbaargs.PIPESIZE/POLL=1.0 FILE=_CORBA_OUT HOST="^corbaargs.HOST" PORT=^corbaargs.PORT &
            FRAMESIZE=^corbaargs.FRAMESIZE OVERRIDE_SRI_SUBSIZE=^corbaargs.OVERRIDE_SRI_SUBSIZE &
            RESOURCE=^corbaargs.RESOURCE PORT_NAME=^corbaargs.PORT_NAME IDL="^corbaargs.IDL"
        if fftargs isNULL then
            set fftout _CORBA_OUT
        else
            set fftout _FFT_OUT
            FFT/NEXP=^fftargs.numAvg _CORBA_OUT ^fftout NFFT=^fftargs.fftsize WIN=^fftargs.window OVER=^fftargs.over NAVG=2
        endif
        if thinargs isNULL then
            set thinout ^fftout
        else
            set thinout _DISPTHIN_OUT
            dispthin/PS=^{thinargs.pipesize}/TL=1 ^fftout ^thinout ^thinargs.refreshrate
        endif

        ! put output pipe into parent macro's PIPE/RAM results table so that PLOT can see it
        SET/PARENT RAM.^{output} RAM.^{thinout}
    PIPE OFF

    ! cleanup entry that we added in parent macro's PIPE/RAM results table
    REMOVE/PARENT RAM.^{output}
ENDMACRO

PROCEDURE processMessage m:msg
    if msg.name eqs "CHANGE_CORBARECEIVER_SETTINGS" then
!     info "Got CHANGE_CORBARECEIVER_SETTINGS msg, data = ^msg.data"
      foreach key in msg.data
        set value msg.data.^{key}
        set reg.corbareceiver.^{key} value
      endfor
    else
      ! forward messages (e.g. from corbareceiver) to registered message handler for this macro (requires NeXtMidas 3.3.1+)
      MESSAGE SEND THIS.MSGID msg
    endif
RETURN
