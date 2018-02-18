import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

@Injectable()
export class SchoolsService {

 constructor(private http: Http) { }

 getSchools() {
   return this.http.get("https://mba-application.appspot.com/mba/users/john.doe@gmail.com").map((response: Response) => response.json().schools);
 }

}