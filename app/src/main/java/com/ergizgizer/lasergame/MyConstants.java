package com.ergizgizer.lasergame;

interface MyConstants {

    String sOnAuthStateChangedSignedIn = "onAuthStateChanged:signed_in:";
    String sOnAuthStateChangedSignedOut = "onAuthStateChanged:signed_out";

    String sCreateAccount = "createAccount:";
    String sSignIn = "signIn";
    String sCreateUserWithEmailOnComplete = "createUserWithEmail:onComplete:";
    String sSignInWithEmailOnComplete = "signInWithEmail:onComplete:";
    String sSignInWithEmailFailed = "signInWithEmail:failed";

    String sFirebaseAuthWithGoogle = "firebaseAuthWithGoogle:";

    String sFacebookOnSuccess = "facebook:onSuccess:";
    String sFacebookOnCancel = "facebook:onCancel";
    String sFacebookOnError = "facebook:onError";
    String sHandleFacebookAccessToken = "handleFacebookAccessToken:";

    String sSignInWithCredentialSuccess = "signInWithCredential:success";
    String sSignInWithCredentialFailure = "signInWithCredential:failure";
}
