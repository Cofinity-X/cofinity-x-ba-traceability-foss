import { BomLifecycleActivatorComponent } from './bom-lifecycle-activator.component';
import { SharedModule } from '@shared/shared.module';
import { BomLifecycleSettingsService, UserSettingView } from "@shared/service/bom-lifecycle-settings.service";
import { renderComponent } from "@tests/test-render.utils";

describe('BomLifecycleActivatorComponent', () => {

    const renderBomLifecycleActivator = (view: UserSettingView = UserSettingView.PARTS) => {
        return renderComponent(BomLifecycleActivatorComponent, {
            imports: [SharedModule],
            providers: [BomLifecycleSettingsService],
            componentProperties: { view },
        });
    };

    it('should create the component', async () => {
        const { fixture } = await renderBomLifecycleActivator(UserSettingView.PARTS);
        const { componentInstance } = fixture;
        expect(componentInstance).toBeTruthy();
    });

    it('should initialize bomLifecycleConfig correctly', async () => {
        const { fixture } = await renderBomLifecycleActivator(UserSettingView.PARTS);
        const { componentInstance } = fixture;
        componentInstance.bomLifecycleConfig = componentInstance.bomLifeCycleUserSetting.getUserSettings(componentInstance.view);

        expect(componentInstance.bomLifecycleConfig.asBuiltActive).toBe(true);
        expect(componentInstance.bomLifecycleConfig.asPlannedActive).toBe(true);
        expect(componentInstance.bomLifecycleConfig.asDesignedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asOrderedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asSupportedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asRecycledActive).toBe(false);
    });

    it('should disable all selected lifecycles when cleared', async () => {
        const { fixture } = await renderBomLifecycleActivator(UserSettingView.PARTS);
        const { componentInstance } = fixture;

        componentInstance.disabledAllLifecycleStates()

        expect(componentInstance.bomLifecycleConfig.asBuiltActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asPlannedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asDesignedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asOrderedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asSupportedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asRecycledActive).toBe(false);

    });

    it('should update the lifecycles when the selection has changed', async () => {
        const { fixture } = await renderBomLifecycleActivator(UserSettingView.PARTS);
        const { componentInstance } = fixture;

        componentInstance.selectedLifecycles = [];

        componentInstance.selectionChanged(['AsDesigned/AsDeveloped', 'AsSupported/AsFlying/AsMaintainted/AsOperated'])

        expect(componentInstance.bomLifecycleConfig.asBuiltActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asPlannedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asRecycledActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asOrderedActive).toBe(false);
        expect(componentInstance.bomLifecycleConfig.asSupportedActive).toBe(true);
        expect(componentInstance.bomLifecycleConfig.asDesignedActive).toBe(true);

        componentInstance.selectionChanged(['AsPlanned', 'AsBuilt'])
    });
});
