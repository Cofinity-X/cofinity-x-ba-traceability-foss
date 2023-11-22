import {DatePipe} from '@angular/common';
import {SemanticDataModel} from '@page/parts/model/parts.model';
import {
    MultiSelectAutocompleteComponent
} from '@shared/components/multi-select-autocomplete/multi-select-autocomplete.component';
import {
    FormatPartSemanticDataModelToCamelCasePipe
} from '@shared/pipes/format-part-semantic-data-model-to-camelcase.pipe';
import {SharedModule} from '@shared/shared.module';
import {renderComponent} from '@tests/test-render.utils';
import {PartTableType} from "@shared/components/table/table.model";
import {Owner} from "@page/parts/model/owner.enum";
import {MatDatepickerInputEvent} from "@angular/material/datepicker";

describe('MultiSelectAutocompleteComponent', () => {
    const renderMultiSelectAutoCompleteComponent = (multiple = true) => {
        const placeholder = "test";
        const options = [SemanticDataModel.PARTASPLANNED, SemanticDataModel.BATCH];

        return renderComponent(MultiSelectAutocompleteComponent, {
            imports: [SharedModule],
            providers: [DatePipe, FormatPartSemanticDataModelToCamelCasePipe],
            componentProperties: {placeholder: placeholder, options: options },
        });
    };

    it('should create the component', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;
        expect(componentInstance).toBeTruthy();
    });

    it('should initialize with empty selectedValue when no input selectedOptions', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;
        expect(componentInstance.selectedValue).toEqual(null);
    });


    it('should emit selectionChange event when options are selected', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.searchElement = 'B'
        const selectedOptions = [SemanticDataModel.BATCH];
        componentInstance.selectedValue = selectedOptions;
        fixture.detectChanges();

        const spy = spyOn(componentInstance.selectionChange, 'emit');
        componentInstance.onSelectionChange({value: selectedOptions});

        expect(spy).toHaveBeenCalledWith(selectedOptions);
    });

    it('should clear values when clickClear is called', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.searchInput = {value: 'initialValue'};
        componentInstance.searchElement = 'initialValue';
        componentInstance.selectedValue = ['initialValue'];
        componentInstance.startDate = new Date('2022-02-04');
        componentInstance.endDate = new Date('2022-02-04');

        componentInstance.clickClear();

        // Assert
        expect(componentInstance.searchElement).toBe('');
        expect(componentInstance.selectedValue).toEqual([]);
    });

    it('should return correct display string when textSearch is true', async () => {

        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.selectedValue = ['TestValue'];
        componentInstance.searchElement = 'TestValue';
        const result = componentInstance.displayValue();

        expect(result).toBe('TestValue');
    });

    it('should return correct display string when textSearch is false and multiple is true', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.selectedValue = ['value1', 'value2', 'value3']; // Replace with your test values
        componentInstance.labelCount = 2; // Replace with the number of labels you expect

        componentInstance.searchElement = 'v'
        componentInstance.options = [
            {value: 'value1', display: 'Display1'},
            {value: 'value2', display: 'Display2'},
            {value: 'value3', display: 'Display3'},
        ];

        const result = componentInstance.displayValue();

        expect(result).toBe('value1 + 2 undefined');
    });

    it('should return correct display string when textSearch is false and multiple is false', async () => {

        const {fixture} = await renderMultiSelectAutoCompleteComponent(false);
        const {componentInstance} = fixture;

        componentInstance.selectedValue = ['value1']; // Replace with your test value

        componentInstance.searchElement = 'v';
        componentInstance.options = [
            {value: 'value1', display: 'Display1'},
            {value: 'value2', display: 'Display2'},
            {value: 'value3', display: 'Display3'},
        ];

        const result = componentInstance.displayValue();

        expect(result).toBe('value1');
    });

    it('should filter options based on value when textSearch is false', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

      componentInstance.filterColumn = 'semanticDataModel'


        componentInstance.filterItem('Display1'); // Filter based on 'Display1'

        expect(componentInstance.options.length).toBe(2);
        expect(componentInstance.options[0]).toBe('PARTASPLANNED');
    });


    it('should select all filtered options when val is true', async () => {

        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.filteredOptions = [
            {value: 'value1', display: 'Display1'},
            {value: 'value2', display: 'Display2'},
            {value: 'value3', display: 'Display3'},
        ];

        componentInstance.selectedValue = ['value1'];

        const val = {checked: true};

        componentInstance.toggleSelectAll(val);

        expect(componentInstance.selectedValue).toEqual(['value1', 'value2', 'value3']);
    });

    it('should deselect options that are not in filteredOptions when val is false', async () => {

        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.filteredOptions = [
            {value: 'value1', display: 'Display1'},
            {value: 'value2', display: 'Display2'},
            {value: 'value3', display: 'Display3'},
        ];

        componentInstance.selectedValue = ['value1', 'value2'];
        const val = {checked: false};
        componentInstance.toggleSelectAll(val);

        expect(componentInstance.selectedValue).toEqual([]);
    });

    it('should emit data correctly when changeEvent of Datepicker is triggered', async () => {

        const {fixture} = await renderMultiSelectAutoCompleteComponent(false);
        const {componentInstance} = fixture;

        const inputValue = new Date('2023-10-12'); // Replace with your desired date

        // Create a mock event with the selected date
        const event: MatDatepickerInputEvent<Date> = {
            value: inputValue,
            target: undefined,
            targetElement: undefined
        };

        // Call the function to test
        componentInstance.startDateSelected(event);



        // Expectations
        expect(componentInstance.searchElement).toEqual('2023-10-12');
    });


    it('should emit date range correctly when changeEvent of Datepicker is triggered', async () => {
      const {fixture} = await renderMultiSelectAutoCompleteComponent(false);
      const {componentInstance} = fixture;

      const startDate = new Date('2023-10-12'); // Replace with your desired date

      // Create a mock event with the selected date
      const startEvent: MatDatepickerInputEvent<Date> = {
        value: startDate,
        target: undefined,
        targetElement: undefined
      };

      // Call the function to test the start date
      componentInstance.startDateSelected(startEvent);

      // Expectations
      expect(componentInstance.searchElement).toBe('2023-10-12');

      const endDate = new Date('2023-10-20'); // Replace with your desired date

      // Create a mock event with the selected date
      const endEvent: MatDatepickerInputEvent<Date> = {
        value: endDate,
        target: undefined,
        targetElement: undefined
      };

      // Call the function to test the end date
      componentInstance.endDateSelected(endEvent);

      // Expectations
      expect(componentInstance.searchElement).toBe('2023-10-12,2023-10-20');

      componentInstance.searchElement = '';
      componentInstance.endDate = null;
        const emptyEndEvent: MatDatepickerInputEvent<Date> = {
            value: null,
            target: undefined,
            targetElement: undefined
        };
      componentInstance.endDateSelected(emptyEndEvent);

      expect(componentInstance.searchElement).toEqual('');
    })

    it('should retrieve correct Owner of partTableType', async() => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        const expectedOwners = [Owner.OWN,Owner.OWN,Owner.SUPPLIER,Owner.SUPPLIER,Owner.CUSTOMER,Owner.CUSTOMER, Owner.UNKNOWN];
        [PartTableType.AS_BUILT_OWN, PartTableType.AS_PLANNED_OWN, PartTableType.AS_BUILT_SUPPLIER, PartTableType.AS_PLANNED_SUPPLIER, PartTableType.AS_BUILT_CUSTOMER,PartTableType.AS_PLANNED_CUSTOMER, null].forEach((tableType, index) => {
            expect(componentInstance.getOwnerOfTable(tableType)).toEqual(expectedOwners[index]);
        })
    })

    it('should filter date with dateFilter()', async function() {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        // @ts-ignore
        componentInstance.searchElement = ["2023-12-10"] as unknown as [];
        componentInstance.dateFilter();
        expect(componentInstance.formControl.value).toEqual(['2023-12-10']);
    })

    it('should return false if autocomplete field is supported', async function() {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        const isSupported = componentInstance.isUnsupportedAutoCompleteField('valid');
        expect(isSupported).toBeFalsy();
    })

    it('should return true if autocomplete field is unsupported', async function() {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        let isSupported = componentInstance.isUnsupportedAutoCompleteField('activeAlerts');
        expect(isSupported).toBeTruthy();
        isSupported = componentInstance.isUnsupportedAutoCompleteField('activeInvestigations');
        expect(isSupported).toBeTruthy();
    })

    it('should subscribe to searchElementChange and call filterItem when delayTimeoutId is present', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;
        // Mock clearTimeout function
        let clearSpy = spyOn(window, 'clearTimeout');
        let filterSpy = spyOn(componentInstance,'filterItem');

        // Initial setup
        componentInstance.delayTimeoutId = 123; // Mocking a non-null delayTimeoutId
        const searchElementValue = 'mockValue';

        // Trigger ngOnInit
        fixture.detectChanges();
        // Emit a value to simulate the searchElementChange event

        componentInstance.searchElementChange.next(searchElementValue);
        // Expectations
        expect(clearSpy).toHaveBeenCalledWith(123);
        expect(filterSpy).toHaveBeenCalledWith(searchElementValue);
        expect(componentInstance.delayTimeoutId).toBeNull();
    });

    it('should change searchtext option', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        const searchElementChangeSpy = spyOn(componentInstance.selectionChange, 'emit');

        componentInstance.selectedValue = ['test'];
        componentInstance.changeSearchTextOption();
        expect(componentInstance.formControl.value).toEqual(['test']);
        expect(searchElementChangeSpy).toHaveBeenCalledWith(['test']);
    })

    fit('should return when calling displayValue() without searchElement', async () => {
        const {fixture} = await renderMultiSelectAutoCompleteComponent();
        const {componentInstance} = fixture;

        componentInstance.searchElement = '';
        componentInstance.displayValue();

        const dValue = componentInstance.displayValue();
        expect(dValue).toEqual(undefined);

    })

});
