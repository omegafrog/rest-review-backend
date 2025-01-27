package org.example.sbb.app.domain.question.answer;

import java.util.Comparator;

public enum SortOption {
    TIME("time"){
        @Override
        public  Comparator<Answer> getComparator(){
            return new Comparator<Answer>() {
                @Override
                public int compare(Answer o1, Answer o2) {
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            };
        }
    }, RECOMMEND("recommend"){
        @Override
        public Comparator<Answer> getComparator() {
            return new Comparator<Answer>() {
                @Override
                public int compare(Answer o1, Answer o2) {
                    return Integer.compare(o1.getVoters().size(), o2.getVoters().size())*(-1);
                }
            };
        }
    }, RECENT_ANSWER("recent_answer"){
        @Override
        public Comparator<Answer> getComparator() {
            return null;
        }
    }, RECENT_COMMENT("recent_comment"){
        @Override
        public Comparator<Answer> getComparator() {
            return null;
        }
    };


    private String value;

    SortOption(String value) {
        this.value = value;
    }

    public static SortOption of(String option) {
        return valueOf(option.toUpperCase());
    }

    abstract public Comparator<Answer> getComparator();
}
