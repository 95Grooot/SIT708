// Generated by view binder compiler. Do not edit!
package com.example.personalizedlearning.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.personalizedlearning.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityResultsBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final Button btnContinue;

  @NonNull
  public final TextView tvOverallFeedback;

  @NonNull
  public final TextView tvQuestion1Result;

  @NonNull
  public final TextView tvQuestion2Result;

  @NonNull
  public final TextView tvQuestion3Result;

  private ActivityResultsBinding(@NonNull ScrollView rootView, @NonNull Button btnContinue,
      @NonNull TextView tvOverallFeedback, @NonNull TextView tvQuestion1Result,
      @NonNull TextView tvQuestion2Result, @NonNull TextView tvQuestion3Result) {
    this.rootView = rootView;
    this.btnContinue = btnContinue;
    this.tvOverallFeedback = tvOverallFeedback;
    this.tvQuestion1Result = tvQuestion1Result;
    this.tvQuestion2Result = tvQuestion2Result;
    this.tvQuestion3Result = tvQuestion3Result;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityResultsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityResultsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_results, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityResultsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_continue;
      Button btnContinue = ViewBindings.findChildViewById(rootView, id);
      if (btnContinue == null) {
        break missingId;
      }

      id = R.id.tv_overall_feedback;
      TextView tvOverallFeedback = ViewBindings.findChildViewById(rootView, id);
      if (tvOverallFeedback == null) {
        break missingId;
      }

      id = R.id.tv_question_1_result;
      TextView tvQuestion1Result = ViewBindings.findChildViewById(rootView, id);
      if (tvQuestion1Result == null) {
        break missingId;
      }

      id = R.id.tv_question_2_result;
      TextView tvQuestion2Result = ViewBindings.findChildViewById(rootView, id);
      if (tvQuestion2Result == null) {
        break missingId;
      }

      id = R.id.tv_question_3_result;
      TextView tvQuestion3Result = ViewBindings.findChildViewById(rootView, id);
      if (tvQuestion3Result == null) {
        break missingId;
      }

      return new ActivityResultsBinding((ScrollView) rootView, btnContinue, tvOverallFeedback,
          tvQuestion1Result, tvQuestion2Result, tvQuestion3Result);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
