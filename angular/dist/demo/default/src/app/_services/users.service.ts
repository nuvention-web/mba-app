import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class UsersSerivce {

 constructor(private http: Http) { }

 getUser() {
    return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com").map((response: Response) => response.json());
 }

 modifyUser(data) {
    console.log(data); 
    this.http.put("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/", data, {headers: header});
 }

}