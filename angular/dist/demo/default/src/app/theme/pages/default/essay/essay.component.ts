///<reference path="../../../../../typings.d.ts"/>
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-essay',
    templateUrl: './essay.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class EssayComponent implements OnInit {
    params = {};
    content = "";

    constructor(private route: ActivatedRoute) {
        this.route.params.subscribe( params =>
            this.params = params

        );
    }

    ngOnInit() {
        var self = this;
        (<any>$)("#draft_editor").summernote({
            placeholder: 'Start typing here...',
            tabsize: 2,
            height: 200
        });
        (<any>$)("#essay-draft-dropdown").select2();
    }
}
