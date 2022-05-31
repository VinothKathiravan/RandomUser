package com.zoho.randomuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zoho.randomuser.RandomUserFragment
import com.zoho.randomuser.RandomUserFragmentDirections
import com.zoho.randomuser.data.RandomUser
import com.zoho.randomuser.databinding.FragmentRandomUserBinding
import com.zoho.randomuser.databinding.ListItemRandomUsersBinding

class RandomUserAdapter : PagingDataAdapter<RandomUser, RandomUserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserViewHolder(
        private val binding: ListItemRandomUsersBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.user?.let {
                    randomUser -> navigateToUserDetail(randomUser, it)
                }
            }
        }

        private fun navigateToUserDetail(user: RandomUser, view: View) {
            val userName = StringBuilder(user.name?.title.orEmpty())
                .append(". ")
                .append(user.name?.first)
                .append(" ")
                .append(user.name?.last)
                .toString()
            val userLocation = StringBuilder(user.location?.city.orEmpty())
                .append(" ")
                .append(user.location?.state.orEmpty())
                .append(", ")
                .append(user.location?.country.orEmpty())
                .toString()

            val direction = RandomUserFragmentDirections.actionUserToDetails(
                userName = userName,
                userEmail = user.email.orEmpty(),
                userAddress = userLocation,
                userPictureUrl = user.picture?.large.orEmpty(),
                userPhone = user.phone.orEmpty(),
                userGender = user.gender.orEmpty(),
                latitude = user.location?.coordinates?.latitude.orEmpty(),
                longitude = user.location?.coordinates?.longitude.orEmpty()
            )

            view.findNavController().navigate(direction)
        }

        fun bind(item: RandomUser) {
            binding.apply {
                user = item
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ListItemRandomUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<RandomUser>() {
    override fun areItemsTheSame(oldItem: RandomUser, newItem: RandomUser): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: RandomUser, newItem: RandomUser): Boolean {
        return oldItem == newItem
    }
}