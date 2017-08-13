package com.ergizgizer.lasergame;

interface MyStaticVariables {

    int OUT_LEFT = 1;
    int OUT_TOP = 2;
    int OUT_RIGHT = 4;
    int OUT_BOTTOM = 8;

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

    String sSquareClicked = "squareClicked:";
    String sLaserRequested = "laserRequested:";
    String sRow = "row:";
    String sColumn = "column:";

    int sDefaultLaserAngle = 90;
}
