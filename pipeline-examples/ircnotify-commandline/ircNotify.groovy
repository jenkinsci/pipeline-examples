stage "notify"

//
// Modify the channel, message etc as needed.
// Some IRC servers require authentication. 
// This specific example does not with the current settings on freenode.
//
node {
    
    sh ''' 
        MSG='This is the message here'
        SERVER=irc.freenode.net
        CHANNEL=#mictest
        USER=mic2234test
    
        (
        echo NICK $USER
        echo USER $USER 8 * : $USER
        sleep 1
        #echo PASS $USER:$MYPASSWORD                                                                                                                                                       
        echo "JOIN $CHANNEL"
        echo "PRIVMSG $CHANNEL" :$MSG
        echo QUIT
        ) | nc $SERVER 6667
        
    '''
    
}
