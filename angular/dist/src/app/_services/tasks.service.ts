import {URL, getCurrentUser, jwt} from "./url-infos";
import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";

@Injectable()
export class TasksService {
    constructor (private http: Http) {

    }

    getTasksAndDeadline() {
        return this.http.get(URL + '/mba/users/' + getCurrentUser(), jwt(1)).map((response: Response) => response.json());
    }

    addTask(activity) {
        console.log(activity);
        console.log(URL + '/mba/users/' + getCurrentUser() + '/tasks');
        return this.http.post(URL + '/mba/users/' + getCurrentUser() + '/tasks', activity, jwt(1));
    }

    updateTask(activity) {
        return this.http.put(URL + '/mba/users/' + getCurrentUser() + '/tasks/' + activity.taskID, activity, jwt(1));
    }
}