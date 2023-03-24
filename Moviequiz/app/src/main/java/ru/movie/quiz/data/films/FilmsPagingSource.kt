package ru.movie.quiz.data.films

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.movie.quiz.data.films.model.Film

class FilmsPagingSource(
    private val filmApi: FilmApi
): PagingSource<Int, Film>() {

    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {
            val page = params.key ?: 1

            val groups = filmApi.getFilmTop(page = page).films

            LoadResult.Page(
                data = groups,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if(groups.size < 13) null else page + 1
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}