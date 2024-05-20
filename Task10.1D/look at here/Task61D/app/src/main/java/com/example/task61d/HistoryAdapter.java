package com.example.task61d;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Question> questions;
    private String type;
    private Context context;
    private TextView time;

    private TextView topic;

    public HistoryAdapter(List<Question> questions, String type, Context context) {
        this.questions = questions;
        this.type = type;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_no_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question);
        holder.arrow_down.setOnClickListener(v -> {
            holder.toggleDetailView();
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView taskDescription;
        private RadioButton answer0, answer1, answer2, answer3;
        private RadioGroup history_answerZone;
        private TextView taskTitle;
        private ImageButton arrow_down;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.generated_task_title);
            time = itemView.findViewById(R.id.time);
            topic = itemView.findViewById(R.id.topic);
            arrow_down = itemView.findViewById(R.id.arrow_down);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Question question) {
            time.setText(question.getTime());
            topic.setText(String.format("| %s", question.getTopic()));
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (taskTitle != null) {
                    taskTitle.setText(format("%d. Question %d", position + 1, position + 1));
                }

            }

            if (taskDescription != null) {
                taskDescription.setText(question.getQuestionText());
            }

            List<String> options = question.getOptions();

            // 检查 options 列表大小
            if (options.size() > 0 && answer0 != null) {
                answer0.setText(options.get(0));
            }

            if (options.size() > 1 && answer1 != null) {
                answer1.setText(options.get(1));
            }

            if (options.size() > 2 && answer2 != null) {
                answer2.setText(options.get(2));
            }

            if (options.size() > 3 && answer3 != null) {
                answer3.setText(options.get(3));
            }

            if (history_answerZone != null) {
                setColorAccordingToIsCorrect(question);
            }
        }

        private void setColorAccordingToIsCorrect(Question question) {
            if (question.getUserAnswer().equals(question.getCorrectAnswer())) {
                switch (question.getCorrectOptionIndex()) {
                    case 0:
                        answer0.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 1:
                        answer1.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 2:
                        answer2.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 3:
                        answer3.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    default:
                        break;
                }
            } else {
                switch (question.getCorrectOptionIndex()) {
                    case 0:
                        answer0.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 1:
                        answer1.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 2:
                        answer2.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case 3:
                        answer3.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    default:
                        break;
                }
                switch (question.getUserAnswerIndex()) {
                    case 0:
                        answer0.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                        break;
                    case 1:
                        answer1.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                        break;
                    case 2:
                        answer2.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                        break;
                    case 3:
                        answer3.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                        break;
                    default:
                        break;
                }
            }
        }

        private void toggleDetailView() {
            ViewGroup parentView = (ViewGroup) itemView;
            if (parentView.getChildCount() > 1) {
                //nothing here
                parentView.removeAllViews();
                LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_history_layout, parentView, true);
                // 初始化详细视图的控件
                taskTitle = itemView.findViewById(R.id.generated_task_title);
                time = itemView.findViewById(R.id.time);
                topic = itemView.findViewById(R.id.topic);
                taskDescription = parentView.findViewById(R.id.taskDescription);
                answer0 = parentView.findViewById(R.id.answer0);
                answer1 = parentView.findViewById(R.id.answer1);
                answer2 = parentView.findViewById(R.id.answer2);
                answer3 = parentView.findViewById(R.id.answer3);
                history_answerZone = parentView.findViewById(R.id.history_answerZone);
                // 绑定详细视图的数据
                bind(questions.get(getAdapterPosition()));
            } else {
                //nothing here
            }
        }
    }
}
