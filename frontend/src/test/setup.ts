import '@testing-library/jest-dom'
import React from 'react'
import { vi } from 'vitest'

vi.mock('primereact/button', () => ({
  Button: (props: any) => {
    const { text, ...buttonProps } = props;
    return React.createElement(
      'button',
      buttonProps,
      props.icon ? React.createElement('span', null, props.icon) : null,
      props.label,
    );
  },
}))

vi.mock('primereact/divider', () => ({
  Divider: (props: any) => React.createElement('hr', props),
}))

vi.mock('primereact/card', () => ({
  Card: (props: any) => React.createElement('div', props, props.children),
}))

vi.mock('primereact/inputnumber', () => ({
  InputNumber: ({ value, onValueChange, onBlur, disabled, inputStyle, ...props }: any) =>
    React.createElement('input', {
      type: 'number',
      value,
      onChange: (e: any) => onValueChange({ value: e.target.value }),
      onBlur,
      disabled,
      style: inputStyle,
      ...props,
    }),
}))

vi.mock('primereact/dropdown', () => ({
  Dropdown: ({ value, onChange, options, optionLabel, optionValue, ...props }: any) => {
    const labelKey = optionLabel ?? 'label';
    const valueKey = optionValue ?? 'value';

    return React.createElement(
      'select',
      {
        value: value ?? '',
        onChange: (e: any) => onChange({ value: e.target.value === '' ? null : e.target.value }),
        ...props,
      },
      options?.map((option: any) => {
        const optionValueActual = option?.[valueKey] ?? option;
        const optionLabelActual = option?.[labelKey] ?? option;
        return React.createElement('option', {
          key: optionValueActual ?? optionLabelActual,
          value: optionValueActual ?? '',
        }, optionLabelActual);
      }),
    );
  },
}))

vi.mock('primereact/dialog', () => ({
  Dialog: ({ visible, children, ...props }: any) =>
    visible ? React.createElement('div', props, children) : null,
}))

vi.mock('primereact/inputtext', () => ({
  InputText: (props: any) => React.createElement('input', { type: 'text', ...props }),
}))

vi.mock('primereact/toolbar', () => ({
  Toolbar: (props: any) => React.createElement('div', props, props.children),
}))

vi.mock('primereact/datatable', () => ({
  DataTable: (props: any) => React.createElement('div', props, props.children),
}))

vi.mock('primereact/column', () => ({
  Column: () => null,
}))

vi.mock('primereact/confirmdialog', () => ({
  ConfirmDialog: () => null,
  confirmDialog: vi.fn(),
}))

vi.mock('primereact/slider', () => ({
  Slider: ({ value, onChange, onSlideEnd, min, max, disabled, ...props }: any) => {
    const clampedValue = Math.max(min, Math.min(max, value));
    return React.createElement('input', {
      type: 'range',
      value: clampedValue,
      onChange: (e: any) => onChange({ value: Number(e.target.value) }),
      onMouseUp: () => onSlideEnd && onSlideEnd({ value: clampedValue }),
      min,
      max,
      disabled,
      ...props,
    })
  },
}))
