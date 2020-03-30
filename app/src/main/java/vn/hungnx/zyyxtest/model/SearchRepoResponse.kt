package vn.hungnx.zyyxtest.model

data class SearchRepoResponse(val total_count:Int,val incomplete_results:Boolean,
                              val items:List<GithubRepoModel>?)