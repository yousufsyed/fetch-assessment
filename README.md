# fetch-assessment
Assessment for fetch:

This is a sample app to showcase android skills for fetching data from network, 
caching temporarily and rendering data onto the Ui using Jetpack Compose.

Following this the structure for this app:

 __________________          ____________________          __________________          __________________         __________________
|                  |------->|                    |------->|                  |------->|                  |------>|                  | 
| **MainActivity** |        | **FetchViewModel** |        | **FetchRewards** |        |  **FetchClient** |       | **FetchService** |
|                  |<-------|                    |<-------|    **Provider**  |<-------|                  |<------|                  |
|__________________|        |____________________|        |__________________|        |__________________|       |__________________|                
                                                              |          ^
                                                           (Future Extension)      
                                                            __v__________|___ 
                                                           |                 |
                                                           |   FetchRewards  |
                                                           |    DataSource   |
                                                           |_________________|
                                                              |          |
                                                              |          |
                                                            __v__________|____ 
                                                           |                  |
                                                           | **FetchRewards** |
                                                           |   **Database**   |
                                                           |__________________|