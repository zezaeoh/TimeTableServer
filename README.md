## Theater  Time Table Server

#### Server that **parses** and **provides** theater's movie timetable

> Database: `mysql`
>
> Servser: `linux (unbuntu)`
>
> Language:
>
> > Server: `java`
> >
> > Client: `java`
> >
> > Crawler: `python`



### Client  scenario

1. Select a movie theater
2. Chose a branch
3. Checking the timetable



### Server scenario

1. Waiting for clients
2. When a client connects, it creates a thread and passes the client 
3. Manages the threads while waiting for the clients.



### Crawler scenario

1. Crawls the movie theater homepage for a specific time each day
2. Stores the timetables in the database.

