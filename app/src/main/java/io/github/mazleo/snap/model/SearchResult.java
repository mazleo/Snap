package io.github.mazleo.snap.model;

import androidx.annotation.NonNull;

import java.util.List;

public class SearchResult<T> {
    private final Class<T> clazz;
    private SearchState searchState;
    private List<PexelsElement> listPexelsElement;

    public SearchResult(SearchState searchState, List<PexelsElement> listPexelsElement, Class<T> clazz) {
        this.searchState = searchState;
        this.listPexelsElement = listPexelsElement;
        this.clazz = clazz;
    }

    public SearchState getSearchState() {
        return searchState;
    }

    public void setSearchState(SearchState searchState) {
        this.searchState = searchState;
    }

    public List<PexelsElement> getListPexelsElement() {
        return listPexelsElement;
    }

    public void setListPexelsElement(List<PexelsElement> listPexelsElement) {
        this.listPexelsElement = listPexelsElement;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder elementListStr = new StringBuilder("[");
        for (int s = 0; s < listPexelsElement.size(); s++) {
            elementListStr.append(listPexelsElement.get(s));
            if (s != listPexelsElement.size() - 1) {
                elementListStr.append(", ");
            }
        }
        elementListStr.append("]");

        return this.searchState + "," + elementListStr.toString();
    }
}
