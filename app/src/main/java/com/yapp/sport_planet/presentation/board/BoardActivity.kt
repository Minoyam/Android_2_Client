package com.yapp.sport_planet.presentation.board

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yapp.sport_planet.R
import com.yapp.sport_planet.data.MenuModel
import com.yapp.sport_planet.data.enums.MenuEnum
import com.yapp.sport_planet.data.enums.SeparatorEnum
import com.yapp.sport_planet.data.vo.ReportRequestVo
import com.yapp.sport_planet.databinding.ActivityBoardBinding
import com.yapp.sport_planet.presentation.base.BaseActivity
import com.yapp.sport_planet.presentation.chatting.UserInfo
import com.yapp.sport_planet.presentation.chatting.view.ChattingActivity
import com.yapp.sport_planet.presentation.custom.CustomDialog
import com.yapp.sport_planet.presentation.home.HomeFragment
import com.yapp.sport_planet.presentation.write.WriteActivity
import com.yapp.sport_planet.util.ClickUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.item_custom_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardActivity : BaseActivity<ActivityBoardBinding>(R.layout.activity_board) {
    private val viewModel by viewModel<BoardViewModel>()

    private val click by lazy { ClickUtil(this.lifecycle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getLongExtra("finishTabBoardId", -1L) != -1L) {
            viewModel.boardId.value = intent.getLongExtra("finishTabBoardId", -1L)
        } else {
            viewModel.boardId.value = intent.getLongExtra(BOARD_ID, -1)
        }


        viewModel.boardId.observe(this, Observer {
            viewModel.getBoardContent()
        })

        viewModel.successFinish
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showToast(it)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addTo(compositeDisposable)

        viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { if (it) showLoading() else hideLoading() }
            .addTo(compositeDisposable)

        viewModel.showBoardHideView
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showToast("게시물이 피드에서 숨김처리 되었습니다.")
                finish()
            }
            .addTo(compositeDisposable)

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                showSuccessDialog()
            }
        })
        viewModel.boardContent.observe(this, Observer { boardContentModel ->
            val isHost = boardContentModel.host.hostId == UserInfo.USER_ID
            binding.btnChatting.visibility =
                if (isHost) View.GONE else View.VISIBLE
            binding.tvTitle.text = boardContentModel.title
            binding.tvBody.text = boardContentModel.content
            binding.tvPeopleCount.text =
                "남은 인원 ${boardContentModel.recruitNumber - boardContentModel.recruitedNumber}명 (${boardContentModel.recruitedNumber}/${boardContentModel.recruitNumber})"
            binding.tvUserName.text = boardContentModel.host.hostName
            binding.tvGroupStatus.text = boardContentModel.groupStatus.name
            binding.tvGroupStatus.setBackgroundResource(if (boardContentModel.groupStatus.code == 0) R.drawable.shape_round_corner_darkblue else R.drawable.shape_round_corner_gray)
            binding.tvExercise.text = boardContentModel.exercise.name
            binding.tvCity.text = boardContentModel.city.name
            binding.tvTag.text = boardContentModel.userTag.name
            val target = boardContentModel.startsAt
            binding.tvDate.text = target.substring(0, 4) + "년 " +
                    target.substring(5, 7) + "월 " +
                    target.substring(8, 10) + "일 " +
                    target.substring(11, 13) + "시 " +
                    target.substring(14, 16) + "분"
            binding.tvPlace.text = boardContentModel.place
            binding.tvLikeCount.text = boardContentModel.host.likes.toString()
            binding.tvHostIntro.text = boardContentModel.host.intro

        })

        binding.toolbar.setSeparator(SeparatorEnum.NONE)
        binding.toolbar.setBackButtonClick(View.OnClickListener { this.finish() })

        binding.btnChatting.setOnClickListener {
            click.run {
                viewModel.makeChattingRoom()
                viewModel.makeChattingRoomResultLiveData.observe(this,
                    Observer {
                        it.getContentIfNotHandled().let { chattingRoom ->
                            if (chattingRoom != null) {
                                val intent =
                                    Intent(applicationContext, ChattingActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBoardContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HomeFragment.REFRESH) {
            if (resultCode == Activity.RESULT_OK) {
                runOnUiThread {
                    viewModel.getBoardContent()
                }
            }
        }
    }

    private fun showReportDialog() {
        val dialog = BoardReportDialog.newInstance()
        dialog.setBoardReportDialogListener(object : BoardReportDialog.BoardReportDialogListener {
            override fun onAccept(index: Long, content: String?) {
                viewModel.boardId.value?.let { boardId ->
                    viewModel.reportBoard(ReportRequestVo(boardId, index, content.toString()))
                }
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setView(R.layout.dialog_success)
            create()
            show()
        }
    }

    companion object {
        const val BOARD_ID = "BOARD_ID"

        fun createInstance(fragment: Fragment, boardId: Long) {
            val intent = Intent(fragment.activity, BoardActivity::class.java)
            intent.putExtra(BOARD_ID, boardId)
            fragment.startActivityForResult(intent, HomeFragment.REFRESH)
        }

        fun createInstance(activity: Activity, boardId: Long) {
            val intent = Intent(activity, BoardActivity::class.java)
            intent.putExtra(BOARD_ID, boardId)
            activity.startActivityForResult(intent, HomeFragment.REFRESH)
        }
    }
}