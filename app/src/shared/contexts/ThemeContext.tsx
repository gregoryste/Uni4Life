import { createContext, useCallback, useState, useMemo, useContext } from "react";
import { ThemeProvider } from "@emotion/react";
import { DarkTheme, LightTheme } from "../themes";
import { Box } from "@mui/system";

interface IThemeContextData {
    themeName: "light" | "dark";
    toggleTheme: () => void;
}

const ThemeContext = createContext({} as IThemeContextData);

export const useAppThemeContext = () => {
    return useContext(ThemeContext);
};

interface IAppThemeProviderProps {
    children: React.ReactNode;
}

export const AppThemeProvider: React.FC<IAppThemeProviderProps> = ({ children }) => {

    const [themeName, setThemeName] = useState<"light" | "dark">("light");

    const toggleTheme = useCallback(() => {
        setThemeName(oldThemeName => oldThemeName === "light" ? "dark" : "light");
    }, []);

    const theme = useMemo(() => {
        if (themeName === "light") return LightTheme;
        
        return DarkTheme;
    }, [themeName]);

    return (
        <ThemeContext.Provider value={{ themeName, toggleTheme }}>
            <ThemeProvider theme={theme}>
            <Box width="100vw" height="100vh" overflow="auto" display="flex" flexDirection="column">
                    { children }
                </Box>
            </ThemeProvider>
        </ThemeContext.Provider>
    );
};