import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class ProfileService {

 constructor(private http: Http) { }

 getProfile() {
    return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/profile").map((response: Response) => response.json());
 }
}