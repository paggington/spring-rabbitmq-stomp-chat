# spring-rabbitmq-stomp-chat
spring rabbitmq angular stomp web-socket chat

Anonimous realtime chat with no-save functionality holds up to 24 users)) yet (it's only ids configuration problem,so you can try to change it by yourself)

Also chat has no rabbit Queue, in next V's gonna be fixed

Added new functionality of saving and fetching pageabled messages, you still cannot get your previous messages in cause of anonimity(saving with session id key), maybe in the next versions will be added profile function, but you can observe all chat messages stashed in came section

You can manage to save messages to NoSQL,but remember of asymptotic complexity of that operations, it's better to have regular db for that kind of operations
