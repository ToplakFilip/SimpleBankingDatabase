<pre>
  ______   __                          __        _______                       __       
 /      \ |  \                        |  \      |       \                     |  \      
|  $$$$$$\ \$$ ______ ____    ______  | $$      | $$$$$$$\  ______   _______  | $$   __ 
| $$___\$$|  \|      \    \  /      \ | $$      | $$__/ $$ |      \ |       \ | $$  /  \
 \$$    \ | $$| $$$$$$\$$$$\|  $$$$$$\| $$      | $$    $$  \$$$$$$\| $$$$$$$\| $$_/  $$
 _\$$$$$$\| $$| $$ | $$ | $$| $$  | $$| $$      | $$$$$$$\ /      $$| $$  | $$| $$   $$ 
|  \__| $$| $$| $$ | $$ | $$| $$__/ $$| $$      | $$__/ $$|  $$$$$$$| $$  | $$| $$$$$$\ 
 \$$    $$| $$| $$ | $$ | $$| $$    $$| $$      | $$    $$ \$$    $$| $$  | $$| $$  \$$\
  \$$$$$$  \$$ \$$  \$$  \$$| $$$$$$$  \$$       \$$$$$$$   \$$$$$$$ \$$   \$$ \$$   \$$
                            | $$                                                        
                            | $$                                                        
                             \$$ 

1 - Create an account 
2 - Log into account 
0 - Exit

[ Successfully logged in ]

1 - Balance 
2 - Add income 
3 - Transfer income
4 - Close account
5 - Log out 
0 - Exit

</pre>

Simple bank system that stores accounts that can add or transfer income.

System uses Java Database Connectivity interface to connect to a SQLite relational database system.
It creates and edits .db file which stores bank account info, account creation, loggin validation, transferring income, etc.
Besides searching through the database, account validation uses Luhn algorithm.