import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { ProfileService } from '../../../../../_services/profile.service';
import { SimpleChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { ScriptLoaderService } from '../../../../../_services/script-loader.service';

@Component({
    selector: 'app-goals',
    templateUrl: './goals.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class GoalsComponent implements OnInit {

    id = 0;
    count:number = 3;
    questions = ["Firstly, why are you interested in pursuing an MBA at this point of your career?",
                "What are your short term goals, following the completion of your MBA?",
                "What are your long term goals, following the completion of your MBA?"];
    questionNames = ["whyMBA", "shortTermGoals", "longTermGoals"];
    datas = ["", "", ""];
    types = [0,0,0];
    @Input() profile:any;
    constructor(private _script: ScriptLoaderService) {
       
    }

    ngOnInit() {
    }

    ngAfterViewInit() {
        //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
        //Add 'implements AfterViewInit' to the class.
        this._script.loadScripts('app-portlets-tools',
            ['assets/demo/default/custom/components/portlets/tools.js']);
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