import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import {ProfileData} from "../profiledata";

@Component({
    selector: 'app-goals',
    templateUrl: '../questions.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GoalsComponent implements OnInit {

    id = 1;
    count:number = 3;
    questions = ["Firstly, why are you interested in pursuing an MBA at this point of your career?",
                "What are your short term goals, following the completion of your MBA?",
                "What are your long term goals, following the completion of your MBA?"];
    questionNames = ["whyMBA", "shortTermGoals", "longTermGoals"];
    datas:any = ["", "", ""];
    types = [0,0,0];
    isInit = true;

    @Input() profile:any;
    constructor(private _profile: ProfileService) {
        console.log('construct');
    }

    ngOnInit() {
    }

    ngOnChanges(changes: SimpleChanges) {
        //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
        //Add '${implements OnChanges}' to the class.
        if (this.isInit && changes['profile'] && this.profile != undefined) {
            for (let i = 0; i < this.count; i++) {
                this.datas[i] = ProfileData.wrap(this.profile[this.questionNames[i]]);
            }
            this.isInit = false;
        }
    }

    save() {
        for (let i = 0; i < this.count; i++) {
            this.profile[this.questionNames[i]] = ProfileData.unwrap(this.datas[i]);
        }
        this._profile.updateProfile(this.profile);
    }
}