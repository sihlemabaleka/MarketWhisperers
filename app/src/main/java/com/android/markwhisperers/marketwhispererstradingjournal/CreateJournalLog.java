package com.android.markwhisperers.marketwhispererstradingjournal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.markwhisperers.marketwhispererstradingjournal.model.Trade;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateJournalLog extends Activity {


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ImageView mTradeImage;
    TextInputEditText mPosition, mPrice, mStopLoss, mTakeProfit, mPair;
    Button btnSubmit;
    ProgressDialog pDialog;
    private File file;
    private Uri fileUri; // file url to store image
    private DatabaseReference mDatabase;

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(Context context) {

        File mediaStorageDir = new File(context.getFilesDir(), "Pictures");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
            Log.i("PDF CREATOR", "Pdf Directory created");
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator
                + "IMG_POP_" + timeStamp + ".jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal_log);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTradeImage = findViewById(R.id.picture);
        mPosition = findViewById(R.id.position);
        mPrice = findViewById(R.id.price);
        mStopLoss = findViewById(R.id.stop_loss);
        mTakeProfit = findViewById(R.id.take_profit);
        mPair = findViewById(R.id.pair);
        btnSubmit = findViewById(R.id.submit);

        pDialog = new ProgressDialog(this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setMessage("Uploading Trade");
        pDialog.setCancelable(false);


        mTradeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    public void selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).into(mTradeImage);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPosition.getText().toString().trim() == "") {
                            mPosition.setError("Required");
                            return;
                        }
                        if (mPrice.getText().toString().trim() == "") {
                            mPrice.setError("Required");
                            return;
                        }
                        if (mStopLoss.getText().toString().trim() == "") {
                            mPrice.setError("Required");
                            return;
                        }

                        pDialog.show();

                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        // Create a storage reference from our app
                        StorageReference storageRef = storage.getReference();


                        StorageReference riversRef = storageRef.child("images/" + resultUri.getLastPathSegment());
                        UploadTask uploadTask = riversRef.putFile(resultUri);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                exception.printStackTrace();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                final Trade trade = new Trade();
                                trade.setTradeImage(taskSnapshot.getMetadata().getDownloadUrl().toString());
                                trade.setTradePair(mPair.getText().toString().trim());
                                trade.setTradePosition(mPosition.getText().toString().trim());
                                trade.setTradePrice(mPrice.getText().toString().trim());
                                trade.setTradeStopLoss(mStopLoss.getText().toString().trim());
                                trade.setTradeTakeProfit(mTakeProfit.getText().toString().trim());

                                double pipsToSL, pipsToTP;

                                if (mPosition.getText().toString().equalsIgnoreCase("Sell")) {
                                    if (mStopLoss.getText().toString().trim() != null) {
                                        pipsToSL = ((Double.parseDouble(mStopLoss.getText().toString().replace(".", "")) - Double.parseDouble(mPrice.getText().toString().trim().replace(".", ""))) / 10);
                                        trade.setTradePipsToStopLoss(String.valueOf(pipsToSL));
                                    }

                                    if (mTakeProfit.getText().toString().trim() != null) {
                                        pipsToTP = ((Double.parseDouble(mPrice.getText().toString().replace(".", "")) - Double.parseDouble(mTakeProfit.getText().toString().trim().replace(".", ""))) / 10);
                                        trade.setTradePipsToTakeProfit(String.valueOf(pipsToTP));
                                    }

                                } else {
                                    if (mStopLoss.getText().toString().trim() != null) {
                                        pipsToSL = ((Double.parseDouble(mPrice.getText().toString().replace(".", "")) - Double.parseDouble(mStopLoss.getText().toString().trim().replace(".", ""))) / 10);
                                        trade.setTradePipsToStopLoss(String.valueOf(pipsToSL));
                                    }

                                    if (mTakeProfit.getText().toString().trim() != null) {
                                        pipsToTP = ((Double.parseDouble(mTakeProfit.getText().toString().replace(".", "")) - Double.parseDouble(mPrice.getText().toString().trim().replace(".", ""))) / 10);
                                        trade.setTradePipsToTakeProfit(String.valueOf(pipsToTP));
                                    }
                                }

                                String key = mDatabase.child("users").child(user.getUid()).child("trades").child("pending").push().getKey();
                                Map<String, Object> postValues = trade.toMap();

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/users/" + uid + "/trades/pending/" + key, postValues);
                                childUpdates.put("/users/" + uid + "/trades/history/" + key, postValues);

                                mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            pDialog.dismiss();
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Couldn't journal this trade. Please try again later", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(File file) {
        return Uri.fromFile(file);
    }

}
