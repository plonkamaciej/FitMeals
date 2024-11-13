import React, { useState } from 'react';
import axios from 'axios';

// Interfejs dla danych użytkownika
interface UserData {
    age: number;
    weight: number;
    height: number;
    gender: 'male' | 'female';
    activityLevel: 'sedentary' | 'lightly_active' | 'moderately_active' | 'very_active' | 'super_active';
}

// Interfejs dla wyników zapotrzebowania
interface DailyRequirements {
    dailyCalorieRequirement: number;
    proteinGoal: number;
    carbsGoal: number;
    fatGoal: number;
}

// Typowanie propsów komponentu
interface SettingsProps {
    userId: number;
}

const Settings: React.FC<SettingsProps> = ({ userId }) => {
    // Stan do przechowywania danych użytkownika
    const [userData, setUserData] = useState<UserData>({
        age: 0,
        weight: 0,
        height: 0,
        gender: 'male',
        activityLevel: 'sedentary',
    });

    // Stan na zapotrzebowanie kaloryczne i makroskładniki
    const [dailyRequirements, setDailyRequirements] = useState<DailyRequirements | null>(null);

    // Obsługa zmiany danych w formularzu
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setUserData({
            ...userData,
            [name]: name === "age" || name === "weight" || name === "height" ? Number(value) : value
        } as UserData);
    };

    // Funkcja obsługująca przesłanie danych i wyliczenie zapotrzebowania
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await axios.post<DailyRequirements>(`/api/users/${userId}/calculateDailyRequirement`, userData);
            setDailyRequirements(response.data); // Ustawienie wyników w stanie
        } catch (error) {
            console.error("Error calculating daily requirements:", error);
        }
    };

    return (
        <div>
            <h2>Ustawienia Celów</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Wiek:</label>
                    <input 
                        type="number" 
                        name="age" 
                        value={userData.age} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div>
                    <label>Waga (kg):</label>
                    <input 
                        type="number" 
                        name="weight" 
                        value={userData.weight} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div>
                    <label>Wzrost (cm):</label>
                    <input 
                        type="number" 
                        name="height" 
                        value={userData.height} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div>
                    <label>Płeć:</label>
                    <select name="gender" value={userData.gender} onChange={handleChange}>
                        <option value="male">Mężczyzna</option>
                        <option value="female">Kobieta</option>
                    </select>
                </div>
                <div>
                    <label>Poziom aktywności:</label>
                    <select name="activityLevel" value={userData.activityLevel} onChange={handleChange}>
                        <option value="sedentary">Siedzący</option>
                        <option value="lightly_active">Lekko aktywny</option>
                        <option value="moderately_active">Umiarkowanie aktywny</option>
                        <option value="very_active">Bardzo aktywny</option>
                        <option value="super_active">Super aktywny</option>
                    </select>
                    
                </div>
                <button type="submit">Przelicz zapotrzebowanie</button>
            </form>

            {dailyRequirements && (
                <div>
                    <h3>Twoje zapotrzebowanie</h3>
                    <p>Kalorie: {dailyRequirements.dailyCalorieRequirement}</p>
                    <p>Białko: {dailyRequirements.proteinGoal} g</p>
                    <p>Węglowodany: {dailyRequirements.carbsGoal} g</p>
                    <p>Tłuszcze: {dailyRequirements.fatGoal} g</p>
                </div>
            )}
        </div>
    );
};

export default Settings;
