<%-- @author Alba Quarto --%>
<select ng-required="p.required" ng-if="!jbUtil.isEmptyObject(p.allowedValues)" class="form-control" id="jbForm-{{p.name}}"
  title="{{p.description}}" name="{{p.name}}" ng-model="updatedVariables[p.name].value" ng-init="updatedVariables[p.name].value = updatedVariables[p.name].value || updatedVariables[p.name].defaultValue">
  <option  ng-repeat="c in p.allowedValues" value="{{c}}"><span>{{p.shownValues[c] || c}}</span></option>
</select>

