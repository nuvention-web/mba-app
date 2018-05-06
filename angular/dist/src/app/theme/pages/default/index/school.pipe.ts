import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'filterSchools'})
export class FilterSchoolsPipe implements PipeTransform {
  transform(allSchools:any[], chosenSchools:any[]) {
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