import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-essays',
    templateUrl: './essays.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class EssaysComponent implements OnInit {
    school = "";

    constructor(private route: ActivatedRoute) {
        this.route.params.subscribe( params =>
            this.school = params.school

        );
    }

    ngOnInit() {
    }
}