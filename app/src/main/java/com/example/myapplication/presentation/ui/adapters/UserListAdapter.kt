package com.example.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.databinding.CardUserBinding
import com.example.myapplication.other.XUtils.formatDate
import com.example.myapplication.other.XUtils.loadImage

class UserListAdapter(
    private val onItemClick: (userId: Long) -> Unit
) :
    PagingDataAdapter<User, UserListAdapter.ViewHolder>(differCallback) {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: User, newItem: User) = (oldItem == newItem)
        }
    }

    inner class ViewHolder(val binding: CardUserBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    private val diff = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CardUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = diff.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = diff.currentList[position]
        val context = holder.binding.image.context

        with(holder) {

            // bind views
            binding.name.text = context.getString(
                R.string.fragment_2_name,
                item.fname,
                item.lname
            )

            binding.joinedAt.text = context.getString(
                R.string.fragment_1_joinedAt,
                item.joinedTimestamp!!.formatDate()
            )

            binding.image.loadImage(
                context = context,
                uri = item.image,
                default = R.drawable.img
            )

            // handle actions
            binding.cardWrapper.setOnClickListener {
                onItemClick(item.id!!)
            }

        }
    }
}