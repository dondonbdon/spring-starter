package dev.bti.starters.models.main;

import dev.bti.starters.enums.CampaignType;
import dev.bti.starters.enums.TargetType;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Campaign {
    CampaignType campaignType;
    String target;
    List<String> targets;
    TargetType targetType;

    public static class Builder {
        private CampaignType type;


        public Builder type(CampaignType type) {
            this.type = type;
            return this;
        }

        public AllBuilder all() {
            return new AllBuilder(type);
        }

        public SingleBuilder single() {
            return new SingleBuilder(type);
        }

        public MultiBuilder multiple() {
            return new MultiBuilder(type);
        }

        @RequiredArgsConstructor
        public static class AllBuilder {
            private final CampaignType type;

            public Campaign build() {
                return new Campaign(type, null, null, TargetType.ALL);
            }
        }

        @RequiredArgsConstructor
        public static class SingleBuilder {

            private final CampaignType type;
            private String target;

            public SingleBuilder target(String email) {
                return this;
            }

            public Campaign build() {
                return new Campaign(type, target, null, TargetType.SINGLE);
            }
        }

        @RequiredArgsConstructor
        public static class MultiBuilder {
            final CampaignType type;

            private String target;
            private List<String> targets;


            public MultiBuilder target(String email) {
                this.target = target;
                return this;
            }

            public MultiBuilder targets(List<String> targets) {
                this.targets = targets;
                return this;
            }

            public Campaign build() {
                return new Campaign(type, target, targets, TargetType.MULTI);
            }
        }

    }
}
