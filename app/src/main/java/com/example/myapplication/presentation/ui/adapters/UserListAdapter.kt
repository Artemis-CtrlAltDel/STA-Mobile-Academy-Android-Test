package com.example.myapplication.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.databinding.CardUserBinding

class UserListAdapter(private val items: List<User>) : Adapter<UserListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CardUserBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<User>() {
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

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        // TODO
    }

    fun setItems(data: List<User>) = differ.submitList(data)
}