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
        expect(componentInstance.bomLifecycleConfig.asBuiltActive).toBe(true);
        expect(componentInstance.bomLifecycleConfig.asPlannedActive).toBe(true);
    });
});
