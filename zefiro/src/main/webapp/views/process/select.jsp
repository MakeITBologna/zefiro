<%-- @author Alba Quarto --%>
<select ng-required="p.required" ng-if="!jbUtil.isEmptyObject(p.allowedValues)" class="form-control" id="jbForm-{{p.name}}"
  title="{{p.description}}" name="{{p.name}}" ng-model="updatedVariables[p.name].value">
  <option ng-repeat="c in p.allowedValues" value="{{c}}">{{c}}</option>
</select>

