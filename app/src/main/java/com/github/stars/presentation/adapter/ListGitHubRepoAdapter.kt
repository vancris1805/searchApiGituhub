package com.github.stars.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.stars.R
import com.github.stars.application.GlideRequests
import com.github.stars.presentation.viewholder.SearchViewHolder
import com.github.stars.repository.model.Items
import com.github.stars.utils.DiffUtilDelegate
import kotlin.properties.Delegates

class ListGitHubRepoAdapter(
    private val glide: GlideRequests,
    private val onItemSelected: (Items) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>(), DiffUtilDelegate<Items> {

    private val onItemClickListener = View.OnClickListener {
        val itemPosition = it.getTag(R.string.position) as Int
        onItemSelected(items[itemPosition])
    }

    override var items: List<Items> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id == n.id }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_search

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.create(parent, glide, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.apply {
            setTag(R.string.position, position)
            setOnClickListener(onItemClickListener)
        }
    }
}