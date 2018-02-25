import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { catchError } from 'rxjs/operators/catchError';

@Injectable()
export class AddSchool {

    private url:string = "https://mba-application.appspot.com/mba/users/john.doe@gmail.com";
    constructor(private http: Http) {}

    handleError(msg, value) {
        console.log(msg + ' : ' + value);
    }
    addSchool(schoolName: string) {
        return this.http.post(this.url, schoolName);
    }  

}