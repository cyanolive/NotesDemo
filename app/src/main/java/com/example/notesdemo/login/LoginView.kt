package com.example.notesdemo.login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notesdemo.R
import com.example.notesdemo.common.ANTENNA_LOOP
import com.example.notesdemo.common.RC_SIGN_IN
import com.example.notesdemo.common.startWithFade
import com.example.notesdemo.login.buildlogic.LoginInjector
import com.example.notesdemo.model.LoginResult
import com.example.notesdemo.note.NoteActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*

class LoginView : Fragment() {

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LoginInjector(requireActivity().application).provideUserViewModelFactory()
        ).get(UserViewModel::class.java)

        (root_fragment_login.background as AnimationDrawable).startWithFade()

        setUpClickListens()
        observeViewModel()

        viewModel.handleEvent(LoginEvent.OnStart)
    }

    private fun observeViewModel() {
        viewModel.authAttempt.observe(
            viewLifecycleOwner,
            Observer {
                startSignInFlow()
            }
        )

        viewModel.startAnimation.observe(
            viewLifecycleOwner,
            Observer {
                imv_antenna_animation.setImageResource(
                    resources.getIdentifier(ANTENNA_LOOP, "drawable", activity?.packageName)
                )
                (imv_antenna_animation.drawable as AnimationDrawable).start()
            }
        )

        viewModel.authButtonText.observe(
            viewLifecycleOwner,
            Observer {
                btn_auth_attempt.text = it
            }
        )

        viewModel.signInStatusText.observe(
            viewLifecycleOwner,
            Observer {
                lbl_login_status_display.text = it
            }
        )

        viewModel.satelliteDrawable.observe(
            viewLifecycleOwner,
            Observer {
                imv_antenna_animation.setImageResource(
                    resources.getIdentifier(it, "drawable", activity?.packageName)
                )
            }
        )
    }

    private fun startSignInFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setUpClickListens() {
        btn_auth_attempt.setOnClickListener { viewModel.handleEvent(LoginEvent.OnAuthButtonClick) }

        imb_toolbar_back.setOnClickListener { startListActivity() }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            startListActivity()
        }
    }

    private fun startListActivity() = requireActivity().startActivity(
        Intent(activity, NoteActivity::class.java)
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var userToken: String? = null
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) userToken = account.idToken
            } catch (e: ApiException) {
                Log.d("LOGIN", e.toString())
            }
        }

        viewModel.handleEvent(
            LoginEvent.OnGoogleSignInResult(
                LoginResult(
                    requestCode,
                    userToken
                )
            )
        )
    }
}
