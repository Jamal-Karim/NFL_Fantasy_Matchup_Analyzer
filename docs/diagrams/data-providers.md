graph TD
Start([Request Player Data]) --> CheckDB{Player in Database?}

    CheckDB -- Yes --> ReturnDB[Return Cached Player]
    CheckDB -- No --> ProviderType{Select Data Provider}
    
    ProviderType -- Mock --> ReadJSON[Read Local JSON Dataset]
    ProviderType -- Real --> ExternalAPI[Fetch Player from  NFL API]
    
    ReadJSON --> MapEntity[Map Player to Entity]
    ExternalAPI --> MapEntity
   
    MapEntity --> Persist[Persist Player to Database]
    Persist --> ReturnNew[Return Domain Player]
   
    ReturnDB --> End([End])
    ReturnNew --> End