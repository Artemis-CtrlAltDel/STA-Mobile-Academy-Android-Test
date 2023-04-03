package com.example.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.databinding.CardUserBinding
import com.example.myapplication.other.formatDate
import com.example.myapplication.other.loadImage

class UserListAdapter(
    val onItemClick: (imageView: ImageView, nameView: TextView, userId: Long) -> Unit
) : PagingDataAdapter<User, UserListAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        return ViewHolder(
            CardUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: User, newItem: User) = (oldItem == newItem)
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(private val binding: CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            val context = binding.image.context

            // bind views
            binding.name.text = context.getString(
                R.string.fragment_2_name,
                user.fname,
                user.lname
            )

            binding.joinedAt.text = context.getString(
                R.string.fragment_1_joinedAt,
                user.joinedTimestamp.formatDate()
            )

            binding.image.loadImage(
                context = context,
                avatar = user.avatar,
                uri = user.image,
                default = R.drawable.img
            )

            // handle actions
            binding.cardWrapper.setOnClickListener {
                onItemClick(binding.image, binding.name, user.id!!)
            }
        }
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}