import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import {ProfileData} from "../profiledata";

@Component({
    selector: 'app-interests',
    templateUrl: '../questions.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class InterestsComponent implements OnInit {

    id = 3;
    @Input() profile:any;
    count:number = 2;
    questions = ["What are your hobbies / interests outside of work/school?",
                "What are you going to bring to the MBA program you are admitted to?",
                ];
    questionNames = ["hobbiesOrInterests", "whatDoYouBring"];
    datas:any = [[],[]];
    types = [1, 0];
    isInit = true;
    constructor(private _profile: ProfileService) {
    
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