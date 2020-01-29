package dev.ugurcan.githubrepos.ui.repolist

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dev.ugurcan.githubrepos.R
import dev.ugurcan.githubrepos.data.Repo

class RepoListAdapter :
    BaseQuickAdapter<Repo, BaseViewHolder>(R.layout.view_repolist_item), LoadMoreModule {

    override fun convert(helper: BaseViewHolder, item: Repo?) {
        helper.setText(R.id.textViewRepoName, item?.name)
        helper.setText(R.id.textViewRepoDescription, item?.description)
        helper.setText(R.id.textViewRepoStarCount, "${item?.starCount}")
    }
}