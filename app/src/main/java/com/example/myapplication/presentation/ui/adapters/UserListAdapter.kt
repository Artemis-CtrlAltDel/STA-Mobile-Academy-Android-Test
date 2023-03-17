package com.example.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.databinding.CardUserBinding
import com.example.myapplication.other.XUtils.formatDate

class UserListAdapter(
    private val onItemClick: (userId: Long) -> Unit
) :
    Adapter<UserListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardUserBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    private val diff = AsyncListDiffer(this, object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    })

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

            Glide.with(context).load(item.image).into(binding.image)

            // handle actions
            binding.cardWrapper.setOnClickListener {
                onItemClick(item.id!!)
            }

        }
    }

    fun setItems(data: List<User>) = diff.submitList(data)
}