import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'filterSchools'})
export class FilterSchoolsPipe implements PipeTransform {
  transform(allSchools:any[], chosenSchools:any[], updated) {
      return allSchools.filter(school => this.isChosen(school, chosenSchools));

  }

  isChosen(school, chosenSchools: any[]) {
    for (var i = 0; i < chosenSchools.length; i++) {
        if (chosenSchools[i].shortName === school.shortName)
            return false;
    }
    return true;
  }
}

@Pipe({name: 'filterSchoolsDeadline'})
export class FilterSchoolsDeadlinePipe implements PipeTransform {
    transform(allSchoolsInfo:any[], chosenValue) {
        if (allSchoolsInfo == null || chosenValue == null) {
            return [];
        }
        var school = null;
        for (var i = 0; i < allSchoolsInfo.length; i++) {
            if (chosenValue === allSchoolsInfo[i].Shortname) {
                console.log(allSchoolsInfo[i]);
                return allSchoolsInfo[i].Deadline;
            }
        }
        return [];
    }
}