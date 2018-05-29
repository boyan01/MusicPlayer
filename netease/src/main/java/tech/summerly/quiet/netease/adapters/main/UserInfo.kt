package tech.summerly.quiet.netease.adapters.main

import kotlinx.android.synthetic.main.netease_item_user.view.*
import tech.summerly.quiet.commonlib.utils.image.setImageUrl
import tech.summerly.quiet.commonlib.utils.support.SimpleTypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.netease.R

class UserInfo {

    val name: String get() = "我要夏天"

    val isSigned: Boolean get() = true

    val profileImageUrl: String get() = ""

}


class UserInfoBinder : SimpleTypedBinder<UserInfo>() {

    override val layoutId: Int
        get() = R.layout.netease_item_user

    override fun onBindViewHolder(holder: ViewHolder, item: UserInfo) = with(holder.itemView) {
        imageUser.setImageUrl(item.profileImageUrl)
        textName.text = item.name
        chipSign.isEnabled = !item.isSigned
        if (item.isSigned) {
            chipSign.setChipTextResource(R.string.netease_signed)
        } else {
            chipSign.setChipTextResource(R.string.netease_sign_in)
        }
    }
}