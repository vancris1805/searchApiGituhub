package com.github.stars.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.github.stars.application.GlideRequests
import com.github.stars.extensions.inflate
import com.github.stars.extensions.inflateImageUrl
import com.github.stars.repository.model.Items
import kotlinx.android.synthetic.main.item_search.view.*

class SearchViewHolder(itemView: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(
            parent: ViewGroup,
            glide: GlideRequests,
            @LayoutRes view: Int
        ): SearchViewHolder {
            return SearchViewHolder(parent.inflate(view), glide)
        }
    }

    fun bind(itemData: Items) {
        with(itemView) {
            nameRepo.text = itemData.fullName
            qtyStars.text = itemData.stargazersCount.toString()
            qtyForks.text = itemData.forksCount.toString()
            authorName.text = itemData.owner.login
            image.inflateImageUrl(itemData.owner.avatarUrl, glide, withPlaceHolder = true)
        }
    }
}
