import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
    selector: 'app-overview',
    templateUrl: './overview.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class OverviewComponent implements OnInit {

    id = 0;
    @Input() profile:any;
    count:number = 9;
    questions = ["Firstly, why are you interested in pursuing an MBA at this point of your career?",
                "What are your short term goals, following the completion of your MBA?",
                "What are your long term goals, following the completion of your MBA?",
                "List some of your experiences that demonstrate your leadership capabilities.",
                "List some of your experiences that demonstrate you are a team player and you work well in a collaborative environment.",
                "List some of your experiences where you failed you achieve what you set out to. What did you learn from these failures?",
                "What are some of your accomplishments that you are most proud of, and why?",
                "What are your hobbies / interests outside of work/school?",
                "What are you going to bring to the MBA program you are admitted to?"];
    questionNames = ["whyMBA", "shortTermGoals", "longTermGoals", "leadershipExperience", "teamPlayerExperience", "failureExperience", "accomplishments","hobbiesOrInterests", "whatDoYouBring"];
    datas:any = ["", "", "", [], [], [], [], [], ""];
    types = [0, 0, 0, 1, 1, 1, 1, 1, 0];
    constructor(private _profile: ProfileService) {

    }

    ngOnInit() {
    }

    ngOnChanges(changes: SimpleChanges) {
        //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
        //Add '${implements OnChanges}' to the class.
        if (changes['profile'] && this.profile != undefined) {
            console.log(this.profile);
            for (var i = 0; i < this.count; i++) {
                this.datas[i] = this.profile[this.questionNames[i]];
            }
        }
    }
}