package com.ergizgizer.lasergame;

interface MyStaticVariables {

    int OUT_LEFT = 1;
    int OUT_TOP = 2;
    int OUT_RIGHT = 4;
    int OUT_BOTTOM = 8;

    int sDefaultLaserAngle = 90;
    String sLaserAngleKey = "laser_angle";
    String sBoardModelKey = "board_model";
    String sIsAllMirrorsDeployed = "is_all_mirrors_deployed";

    String sOnAuthStateChangedSignedIn = "onAuthStateChanged:signed_in:";
    String sOnAuthStateChangedSignedOut = "onAuthStateChanged:signed_out";

    String sCreateAccount = "createAccount:";
    String sSignIn = "signIn";
    String sCreateUserWithEmailSuccess = "createUserWithEmail:success";
    String getsCreateUserWithEmailFailed = "createUserWithEmail:failed";
    String sSignInWithEmailSuccess = "signInWithEmail:success";
    String sSignInWithEmailFailed = "signInWithEmail:failed";
    String sSendEmailVerification = "sendEmailVerification";
    String sVerificationEmailSentTo = "Verification email sent to ";
    String sSendVerificationEmailFailed = "Failed to send verification email.";

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

    String sIntersectedAt = "intersected at:";
    String sBlockedAt = "blocked at:";
}
