import { Injectable } from '@angular/core';
import { Http, Response,Headers } from '@angular/http';
import { HttpClient, HttpHeaders } from '@angular/common/http';

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class SchoolsService {

 constructor(private http: Http) { }

 getSchools() {
   return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com").map((response: Response) => response.json().schools);
 }
 
 getAllSchools() {
   return this.http.get("https://mba-application.appspot.com/mba" +
       "/schools").map((response: Response) => response.json());
 }

 userDeleteSchool(schoolName: string) {
   this.http.delete("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school/" + schoolName);
 }

 userAddSchool(schoolName: string) {
   this.http.put("https://mba-application.appspot.com/mba/users/john.doe@gmail.com/school", {"schools": [schoolName]}
   , {headers: header});
 }
}