# CHANGELOG

__JL 6.24.2020__

- Create SRS doc

__JL 6.25.2020__

- Add to SRS
- Design UI
- Begin UML Class Diagram

__JL 6.27.2020__

- Complete initial UML class diagram
- Create initial application
- Complete launch activity
- Partially complete SearchActivity
- Partially ensure screen, density, and orientation compatibility

__JL 6.28.2020__

- SearchActivity
    - Fix animated logo bug on back press
    - Override search button submission to do nothing
    - Switch to next activity on search query change
    - Close activity on opening next activity
- ImageSearchActivity
    - Implement layout
    - Get previous search query and populate current one
    - Implement transition animation to this activity
    - Add query text listener

__JL 6.30.2020__

- Create SearchInfo class
    - Add API key
- Implement util classes
- Implement model classes
    - PexelsElement
    - PexelsImage
    - SearchState
    - SearchResult
- Add RxAndroid dependency
- Add Retrofit dependency
- Create initial network components
- Add Gson dependency

__JL 7.1.2020__

- Implement search result deserialization
- Implement ImageService interface
- Implement fetch image bitmaps
- Implement ImageUtility class
- Implement FetchThumbnailsWebService (formerly FetchImagesWebService)
- Implement return search result

__JL 7.2.2020__

- Implement search components clean up
- Implement passing of no result
- Implement passing of error
- Add viewmodel inheritance
- Implement display of search results
- Change image grid (recyclerview) layout height
- Implement progress bar

__JL 7.7.2020__

- Make search bar borders thinner
- Implement next page search upon scrolling to bottom
- Update SRS
- Both query submit and change initiates a search process
- Increase thumbnail download timeouts
- Use toasts to indicate error
- Indicate on no results
- Fix empty query string bug

__JL 7.8.2020__

- Implement parcelables
- Implement ViewImageActivity

__JL 7.9.2020__

- Fix image grid selection bugs
- Allow canceling of image retrieval
- Change focus from searchbar to image grid on query submit
- Implement image info display in view image
- Change logo image next to search bar to smaller image
- Create and set launcher icons
- Clean up activity classes and some others

__JL 7.10.2020__

- Add README
- Clean up all files
- Finalize docs and project
- Fix fragment dialog bug; crash on configuration change
- Add release apk