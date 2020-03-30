package vn.hungnx.zyyxtest.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_repo_fav.view.*
import vn.hungnx.zyyxtest.R
import vn.hungnx.zyyxtest.model.GithubRepoModel

class RepoFavAdapter(val listItem:ArrayList<GithubRepoModel>,val onClickItem: OnClickItem) : RecyclerView.Adapter<RepoFavAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_repo_fav,parent,false))
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(listItem[position])
        holder.itemView.setOnClickListener {
            onClickItem.onClickItem(listItem[position])
        }
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bindData(item:GithubRepoModel){
            itemView.tv_name.text = item.name
        }
    }
}