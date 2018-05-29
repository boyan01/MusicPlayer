package tech.summerly.quiet.netease.adapters.main

import android.view.LayoutInflater
import kotlinx.android.synthetic.main.netease_content_item_user.view.*
import kotlinx.android.synthetic.main.netease_content_item_user_not_login.view.*
import kotlinx.android.synthetic.main.netease_item_user.view.*
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.utils.image.setImageUrl
import tech.summerly.quiet.commonlib.utils.support.SimpleTypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.ui.LoginActivity

internal class UserInfo {

    val isLogin: Boolean get() = false

    val name: String get() = "我要夏天"

    val isSigned: Boolean get() = true

    val profileImageUrl: String get() = ""

}


internal class UserInfoBinder : SimpleTypedBinder<UserInfo>() {

    override val layoutId: Int
        get() = R.layout.netease_item_user

    override fun onBindViewHolder(holder: ViewHolder, item: UserInfo) = with(holder.itemView) {
        cardView.removeAllViews()
        if (item.isLogin) {
            LayoutInflater.from(context).inflate(R.layout.netease_content_item_user, cardView)
            imageUser.setImageUrl(item.profileImageUrl)
            textName.text = item.name
            chipSign.isEnabled = !item.isSigned
            if (item.isSigned) {
                chipSign.setChipTextResource(R.string.netease_signed)
            } else {
                chipSign.setChipTextResource(R.string.netease_sign_in)
            }
        } else {
            LayoutInflater.from(context).inflate(R.layout.netease_content_item_user_not_login, cardView)
            buttonLogin.setOnClickListener {
                context.startActivity<LoginActivity>()
            }
        }
        Unit
    }
}